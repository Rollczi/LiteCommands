package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.event.CandidateExecutorFoundEvent;
import dev.rollczi.litecommands.command.executor.event.CandidateExecutorMatchEvent;
import dev.rollczi.litecommands.command.executor.event.CommandPostExecutionEvent;
import dev.rollczi.litecommands.command.executor.event.CommandPreExecutionEvent;
import dev.rollczi.litecommands.command.executor.flow.ExecuteFlow;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.invalidusage.InvalidUsageException;
import dev.rollczi.litecommands.event.EventPublisher;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.requirement.RequirementCondition;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.handler.result.ResultHandleService;
import dev.rollczi.litecommands.invalidusage.InvalidUsage.Cause;
import dev.rollczi.litecommands.schematic.Schematic;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import dev.rollczi.litecommands.schematic.SchematicInput;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.requirement.RequirementMatch;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.strict.StrictService;
import dev.rollczi.litecommands.validator.ValidatorResult;
import dev.rollczi.litecommands.validator.ValidatorService;
import dev.rollczi.litecommands.validator.requirement.RequirementValidator;
import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class CommandExecuteService<SENDER> {

    private final ValidatorService<SENDER> validatorService;
    private final ResultHandleService<SENDER> resultResolver;
    private final Scheduler scheduler;
    private final SchematicGenerator<SENDER> schematicGenerator;
    private final ScheduledRequirementResolver<SENDER> scheduledRequirementResolver;
    private final WrapperRegistry wrapperRegistry;
    private final EventPublisher publisher;
    private final StrictService strictService;

    public CommandExecuteService(
        ValidatorService<SENDER> validatorService,
        ResultHandleService<SENDER> resultResolver,
        Scheduler scheduler,
        SchematicGenerator<SENDER> schematicGenerator,
        ParserRegistry<SENDER> parserRegistry,
        ContextRegistry<SENDER> contextRegistry,
        WrapperRegistry wrapperRegistry,
        BindRegistry bindRegistry,
        EventPublisher publisher, StrictService strictService
    ) {
        this.validatorService = validatorService;
        this.resultResolver = resultResolver;
        this.scheduler = scheduler;
        this.schematicGenerator = schematicGenerator;
        this.wrapperRegistry = wrapperRegistry;
        this.publisher = publisher;
        this.strictService = strictService;
        this.scheduledRequirementResolver = new ScheduledRequirementResolver<>(contextRegistry, parserRegistry, bindRegistry, scheduler);
    }

    public CompletableFuture<CommandExecuteResult> execute(Invocation<SENDER> invocation, ParseableInputMatcher<?> matcher, CommandRoute<SENDER> commandRoute) {
        return execute0(invocation, matcher, commandRoute)
            .thenApply(result -> publishAndApplyEvent(invocation, commandRoute, result))
            .thenCompose(executeResult -> scheduler.supply(SchedulerPoll.MAIN, () -> this.handleResult(invocation, executeResult)))
            .exceptionally(new LastExceptionHandler<>(resultResolver, invocation));
    }

    @SuppressWarnings("unchecked")
    private CommandExecuteResult publishAndApplyEvent(Invocation<SENDER> invocation, CommandRoute<SENDER> route, CommandExecuteResult result) {
        if (this.publisher.hasSubscribers(CommandPostExecutionEvent.class)) {
            CommandExecutor<SENDER> executor = (CommandExecutor<SENDER>) result.getExecutor();
            result = this.publisher.publish(new CommandPostExecutionEvent<>(invocation, route, executor, result)).getResult();
        }

        return result;
    }

    private CommandExecuteResult handleResult(Invocation<SENDER> invocation, CommandExecuteResult executeResult) {
        Throwable throwable = executeResult.getThrowable();
        if (throwable != null) {
            resultResolver.resolve(invocation, throwable);
        }

        Object result = executeResult.getResult();
        if (result != null) {
            resultResolver.resolve(invocation, result);
        }

        Object error = executeResult.getError();
        if (error != null) {
            resultResolver.resolve(invocation, error);
        }

        return executeResult;
    }

    @SuppressWarnings("unchecked")
    private Object mapResult(Object error, CommandRoute<SENDER> commandRoute, CommandExecuteResult executeResult, Invocation<SENDER> invocation) {
        if (error instanceof Cause) {
            Cause cause = (Cause) error;
            @Nullable CommandExecutor<SENDER> executor = (CommandExecutor<SENDER>) executeResult.getExecutor();
            Schematic schematic = schematicGenerator.generate(new SchematicInput<>(commandRoute, executor, invocation));

            return new InvalidUsage<>(cause, commandRoute, schematic);
        }

        return error;
    }

    private <MATCHER extends ParseableInputMatcher<MATCHER>> CompletableFuture<CommandExecuteResult> execute0(
        Invocation<SENDER> invocation,
        ParseableInputMatcher<MATCHER> matcher,
        CommandRoute<SENDER> commandRoute
    ) {
        return this.execute(commandRoute.getExecutors().iterator(), invocation, matcher, commandRoute, null);
    }

    private <MATCHER extends ParseableInputMatcher<MATCHER>> CompletableFuture<CommandExecuteResult> execute(
        Iterator<CommandExecutor<SENDER>> executors,
        Invocation<SENDER> invocation,
        ParseableInputMatcher<MATCHER> matcher,
        CommandRoute<SENDER> commandRoute,
        @Nullable FailedReason last
    ) {
        // Handle failed
        if (!executors.hasNext()) {
            // Route valid
            Flow validate = validatorService.validate(invocation, commandRoute);
            if (validate.isTerminate() || validate.isStopCurrent()) {
                return completedFuture(CommandExecuteResult.failed(validate.getReason()));
            }

            CommandExecutor<SENDER> executor = commandRoute.getExecutors().isEmpty()
                ? null :
                commandRoute.getExecutors().last();

            if (last != null && last.hasResult()) {
                return completedFuture(CommandExecuteResult.failed(executor, last));
            }

            return completedFuture(CommandExecuteResult.failed(InvalidUsage.Cause.UNKNOWN_COMMAND));
        }

        CommandExecutor<SENDER> executor = executors.next();

        if (publisher.hasSubscribers(CandidateExecutorFoundEvent.class)) {
            CandidateExecutorFoundEvent<SENDER> foundEvent = publisher.publish(new CandidateExecutorFoundEvent<>(invocation, executor));
            if (foundEvent.getFlow() == ExecuteFlow.STOP) {
                return completedFuture(CommandExecuteResult.failed(executor, foundEvent.getFlowResult()));
            }

            if (foundEvent.getFlow() == ExecuteFlow.SKIP) {
                return this.execute(executors, invocation, matcher, commandRoute, foundEvent.getFlowResult());
            }
        }

        // Handle matching arguments
        return this.match(executor, invocation, matcher.copy()).thenCompose(match -> {
            if (publisher.hasSubscribers(CandidateExecutorMatchEvent.class)) {
                CandidateExecutorMatchEvent<SENDER> matchEvent = publisher.publish(new CandidateExecutorMatchEvent<>(invocation, executor, match));
                if (matchEvent.getFlow() == ExecuteFlow.STOP) {
                    return completedFuture(CommandExecuteResult.failed(executor, matchEvent.getFlowResult()));
                }

                if (matchEvent.getFlow() == ExecuteFlow.SKIP) {
                    return this.execute(executors, invocation, matcher, commandRoute, matchEvent.getFlowResult());
                }
            }

            if (match.isFailed()) {
                FailedReason current = match.getFailedReason();

                if (current.hasResult()) {
                    return this.execute(executors, invocation, matcher, commandRoute, current);
                }

                return this.execute(executors, invocation, matcher, commandRoute, last);
            }

            if (publisher.hasSubscribers(CommandPreExecutionEvent.class)) {
                CommandPreExecutionEvent<SENDER> executionEvent = publisher.publish(new CommandPreExecutionEvent<>(invocation, executor));
                if (executionEvent.getFlow() == ExecuteFlow.STOP) {
                    return completedFuture(CommandExecuteResult.failed(executor, executionEvent.getFlowResult()));
                }

                if (executionEvent.getFlow() == ExecuteFlow.SKIP) {
                    return this.execute(executors, invocation, matcher, commandRoute, executionEvent.getFlowResult());
                }
            }

            // Execution
            SchedulerPoll type = executor.meta().get(Meta.POLL_TYPE);

            return scheduler.supply(type, () -> execute(match, executor));
        }).exceptionally(throwable -> toThrown(executor, throwable));
    }

    private CommandExecuteResult execute(CommandExecutorMatchResult match, CommandExecutor<SENDER> executor) {
        try {
            return match.executeCommand();
        }
        catch (LiteCommandsException exception) {
            if (exception.getCause() instanceof InvalidUsageException) { //TODO: Use invalid usage handler (when InvalidUsage.Cause is mapped to InvalidUsage)
                return CommandExecuteResult.failed(executor, ((InvalidUsageException) exception.getCause()).getErrorResult());
            }

            return CommandExecuteResult.thrown(executor, exception);
        }
        catch (Throwable error) {
            return CommandExecuteResult.thrown(executor, error);
        }
    }

    private CommandExecuteResult toThrown(CommandExecutor<SENDER> executor, Throwable throwable) {
        if (throwable instanceof CompletionException) {
            return CommandExecuteResult.thrown(executor, throwable.getCause());
        }

        return CommandExecuteResult.thrown(executor, throwable);
    }

    private <MATCHER extends ParseableInputMatcher<MATCHER>> CompletableFuture<CommandExecutorMatchResult> match(
        CommandExecutor<SENDER> executor,
        Invocation<SENDER> invocation,
        MATCHER matcher
    ) {
        List<ScheduledRequirement<?>> scheduledRequirements = scheduledRequirementResolver.prepareRequirements(executor, invocation, matcher);
        return match(invocation, executor, new ArrayList<>(), scheduledRequirements.listIterator(), matcher);
    }

    private CompletableFuture<CommandExecutorMatchResult> match(
        Invocation<SENDER> invocation,
        CommandExecutor<SENDER> executor,
        List<RequirementMatch> matches,
        Iterator<ScheduledRequirement<?>> requirementIterator,
        ParseableInputMatcher<?> matcher
    ) {
        if (!requirementIterator.hasNext()) {
            ParseableInputMatcher.EndResult endResult = matcher.endMatch(strictService.isStrict(executor));

            if (!endResult.isSuccessful()) {
                return completedFuture(CommandExecutorMatchResult.failed(endResult.getFailedReason()));
            }

            RequirementsResult.Builder<SENDER> resultBuilder = RequirementsResult.builder(invocation);

            for (RequirementMatch success : matches) {
                resultBuilder.add(success.getRequirement().getName(), success);
            }

            return completedFuture(executor.match(resultBuilder.build()));
        }

        ScheduledRequirement<?> scheduledRequirement = requirementIterator.next();

        return scheduledRequirement.runMatch().thenCompose(result -> result.asFuture()).thenCompose(requirementResult -> {
            Requirement<?> requirement = scheduledRequirement.getRequirement();

            if (requirementResult.isFailed()) {
                WrapFormat<?, ?> wrapperFormat = requirement.getWrapperFormat();
                Object failedReason = requirementResult.getFailedReason();
                Wrapper wrapper = wrapperRegistry.getWrappedExpectedFactory(wrapperFormat);

                if (failedReason == Cause.MISSING_ARGUMENT && wrapper.canCreateEmpty()) {
                    Wrap<?> wrap = wrapper.createEmpty(wrapperFormat);

                    matches.add(new RequirementMatch(requirement, wrap));
                    return match(invocation, executor, matches, requirementIterator, matcher);
                }

                return CompletableFuture.completedFuture(CommandExecutorMatchResult.failed(failedReason));
            }

            if (requirementResult.isSuccessfulNull()) {
                matches.add(toMatch(requirement, null));
                return match(invocation, executor, matches, requirementIterator, matcher);
            }

            Object success = requirementResult.getSuccess();

            List<RequirementValidator<?, ?>> validators = requirement.meta().get(Meta.REQUIREMENT_VALIDATORS);

            for (RequirementValidator<?, ?> validator : validators) {
                ValidatorResult validatorResult = validateRequirement(invocation, executor, requirement, success, validator);

                if (validatorResult.isInvalid()) {
                    return completedFuture(CommandExecutorMatchResult.failed(validatorResult.getInvalidResult()));
                }
            }

            matches.add(toMatch(requirement, success));
            return match(invocation, executor, matches, requirementIterator, matcher).thenApply(executorMatchResult -> {
                for (RequirementCondition condition : requirementResult.getConditions()) {
                    Optional<FailedReason> failedReason = condition.check(invocation, executorMatchResult);

                    if (failedReason.isPresent()) {
                        return CommandExecutorMatchResult.failed(failedReason.get());
                    }
                }

                return executorMatchResult;
            });
        });
    }


    @SuppressWarnings("unchecked")
    private <T> ValidatorResult validateRequirement(
        Invocation<SENDER> invocation,
        CommandExecutor<SENDER> executor,
        Requirement<?> requirement,
        T value,
        RequirementValidator<?, ?> validator
    ) {
        Requirement<T> castedRequirement = (Requirement<T>) requirement;
        RequirementValidator<SENDER, T> casted = (RequirementValidator<SENDER, T>) validator;

        return casted.validate(invocation, executor, castedRequirement, value);
    }

    @SuppressWarnings("unchecked")
    private <R extends Requirement<? extends T>, T> RequirementMatch toMatch(R requirement, T result) {
        WrapFormat<T, ?> wrapperFormat = (WrapFormat<T, ?>) requirement.getWrapperFormat();
        Wrap<T> wrap = wrapperRegistry.wrap(() -> result, wrapperFormat);

        return new RequirementMatch(requirement, wrap);
    }


}
