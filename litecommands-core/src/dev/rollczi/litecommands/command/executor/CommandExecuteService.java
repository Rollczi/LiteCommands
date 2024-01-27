package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.requirement.Requirement;
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
import dev.rollczi.litecommands.validator.ValidatorResult;
import dev.rollczi.litecommands.validator.ValidatorService;
import dev.rollczi.litecommands.validator.requirment.RequirementValidator;
import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import java.util.ArrayList;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ListIterator;
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

    public CommandExecuteService(ValidatorService<SENDER> validatorService, ResultHandleService<SENDER> resultResolver, Scheduler scheduler, SchematicGenerator<SENDER> schematicGenerator, ParserRegistry<SENDER> parserRegistry, ContextRegistry<SENDER> contextRegistry, WrapperRegistry wrapperRegistry, BindRegistry bindRegistry) {
        this.validatorService = validatorService;
        this.resultResolver = resultResolver;
        this.scheduler = scheduler;
        this.schematicGenerator = schematicGenerator;
        this.wrapperRegistry = wrapperRegistry;
        this.scheduledRequirementResolver = new ScheduledRequirementResolver<>(contextRegistry, parserRegistry, bindRegistry, scheduler);
    }

    public CompletableFuture<CommandExecuteResult> execute(Invocation<SENDER> invocation, ParseableInputMatcher<?> matcher, CommandRoute<SENDER> commandRoute) {
        return execute0(invocation, matcher, commandRoute)
            .thenApply(commandExecuteResult -> mapResult(commandRoute, commandExecuteResult, invocation))
            .thenCompose(executeResult -> scheduler.supply(SchedulerPoll.MAIN, () -> {
                this.handleResult(invocation, executeResult);

                return executeResult;
            }))
            .exceptionally(new LastExceptionHandler<>(resultResolver, invocation));
    }

    private void handleResult(Invocation<SENDER> invocation, CommandExecuteResult executeResult) {
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
    }

    // TODO Support mapping of result in result resolver
    private CommandExecuteResult mapResult(CommandRoute<SENDER> commandRoute, CommandExecuteResult executeResult, Invocation<SENDER> invocation) {
        Object result = executeResult.getResult();
        if (result != null) {
            return CommandExecuteResult.success(executeResult.getExecutor(), mapResult(result, commandRoute, executeResult, invocation));
        }

        Object error = executeResult.getError();
        if (error != null) {
            return CommandExecuteResult.failed(executeResult.getExecutor(), mapResult(error, commandRoute, executeResult, invocation));
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
        return this.execute(commandRoute.getExecutors().listIterator(), invocation, matcher, commandRoute, null);
    }

    private <MATCHER extends ParseableInputMatcher<MATCHER>> CompletableFuture<CommandExecuteResult> execute(
        ListIterator<CommandExecutor<SENDER>> executors,
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
                return completedFuture(CommandExecuteResult.failed(null, validate.getReason()));
            }

            // continue handle failed
            CommandExecutor<SENDER> executor = executors.hasPrevious() ? executors.previous() : null;

            if (last != null && last.hasResult()) {
                return completedFuture(CommandExecuteResult.failed(executor, last));
            }

            return completedFuture(CommandExecuteResult.failed(executor, InvalidUsage.Cause.UNKNOWN_COMMAND));
        }

        CommandExecutor<SENDER> executor = executors.next();
        // Handle matching arguments
        return this.match(executor, invocation, matcher.copy()).thenCompose(match -> {
            if (match.isFailed()) {
                FailedReason current = match.getFailedReason();

                if (current.hasResult()) {
                    return this.execute(executors, invocation, matcher, commandRoute, current);
                }

                return this.execute(executors, invocation, matcher, commandRoute, last);
            }

            // Handle validation
            Flow flow = this.validatorService.validate(invocation, executor);

            if (flow.isTerminate()) {
                return completedFuture(CommandExecuteResult.failed(executor, flow.getReason()));
            }

            if (flow.isStopCurrent()) {
                return this.execute(executors, invocation, matcher, commandRoute, flow.failedReason());
            }

            // Execution
            SchedulerPoll type = executor.meta().get(Meta.POLL_TYPE);

            return scheduler.supply(type, () -> {
                try {
                    return match.executeCommand();
                } catch (Throwable error) {
                    return CommandExecuteResult.thrown(executor, error);
                }
            });
        }).exceptionally(throwable -> toThrown(executor, throwable));
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
            ParseableInputMatcher.EndResult endResult = matcher.endMatch();

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

        return scheduledRequirement.runMatch().thenCompose((requirementResult) -> {
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
            return match(invocation, executor, matches, requirementIterator, matcher);
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
