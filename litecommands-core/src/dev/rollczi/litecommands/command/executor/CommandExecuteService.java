package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.requirement.BindRequirement;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.requirement.RequirementResult;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.handler.result.ResultHandleService;
import dev.rollczi.litecommands.invalidusage.InvalidUsage.Cause;
import dev.rollczi.litecommands.schematic.Schematic;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import dev.rollczi.litecommands.schematic.SchematicInput;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import panda.std.Result;

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
    private final ParserRegistry<SENDER> parserRegistry;
    private final ContextRegistry<SENDER> contextRegistry;
    private final WrapperRegistry wrapperRegistry;
    private final BindRegistry bindRegistry;

    private final BiMap<Class<?>, ArgumentKey, ParserSet<SENDER, ?>> cachedParserSets = new BiHashMap<>();

    public CommandExecuteService(ValidatorService<SENDER> validatorService, ResultHandleService<SENDER> resultResolver, Scheduler scheduler, SchematicGenerator<SENDER> schematicGenerator, ParserRegistry<SENDER> parserRegistry, ContextRegistry<SENDER> contextRegistry, WrapperRegistry wrapperRegistry, BindRegistry bindRegistry) {
        this.validatorService = validatorService;
        this.resultResolver = resultResolver;
        this.scheduler = scheduler;
        this.schematicGenerator = schematicGenerator;
        this.parserRegistry = parserRegistry;
        this.contextRegistry = contextRegistry;
        this.wrapperRegistry = wrapperRegistry;
        this.bindRegistry = bindRegistry;
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
        List<CompletableFuture<ScheduledRequirement<?>>> requirements = getRequirements(executor, invocation, matcher);

        return CompletableFuture.allOf(requirements.toArray(new CompletableFuture[0])).thenApply(unused -> {
            List<RequirementMatch> matches = new ArrayList<>();

            for (CompletableFuture<ScheduledRequirement<?>> completableFuture : requirements) {
                ScheduledRequirement<?> scheduledRequirement = completableFuture.join();
                Requirement<?> requirement = scheduledRequirement.requirement;

                if (scheduledRequirement.getMatch().isFailed()) {
                    WrapFormat<?, ?> wrapperFormat = requirement.getWrapperFormat();
                    Object failedReason = scheduledRequirement.getMatch().getFailedReason();
                    Wrapper wrapper = wrapperRegistry.getWrappedExpectedFactory(wrapperFormat);

                    if (failedReason == Cause.MISSING_ARGUMENT && wrapper.canCreateEmpty()) {
                        Wrap<?> wrap = wrapper.createEmpty(wrapperFormat);

                        matches.add(new RequirementMatch(requirement, wrap));
                        continue;
                    }

                    return CommandExecutorMatchResult.failed(failedReason);
                }

                if (scheduledRequirement.getMatch().isSuccessfulNull()) {
                    matches.add(toMatch(requirement, null));
                    continue;
                }

                Object success = scheduledRequirement.getMatch().getSuccess();

                List<RequirementValidator<?, ?>> validators = requirement.meta().get(Meta.REQUIREMENT_VALIDATORS);

                for (RequirementValidator<?, ?> validator : validators) {
                    ValidatorResult validatorResult = validateRequirement(invocation, executor, requirement, success, validator);

                    if (validatorResult.isInvalid()) {
                        return CommandExecutorMatchResult.failed(validatorResult.getInvalidResult());
                    }
                }

                matches.add(toMatch(requirement, success));
            }

            ParseableInputMatcher.EndResult endResult = matcher.endMatch();

            if (!endResult.isSuccessful()) {
                return CommandExecutorMatchResult.failed(endResult.getFailedReason());
            }

            RequirementsResult.Builder<SENDER> resultBuilder = RequirementsResult.builder(invocation);

            for (RequirementMatch success : matches) {
                resultBuilder.add(success.getRequirement().getName(), success);
            }

            return executor.match(resultBuilder.build());
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

    @NotNull
    private <MATCHER extends ParseableInputMatcher<MATCHER>> List<CompletableFuture<ScheduledRequirement<?>>> getRequirements(CommandExecutor<SENDER> executor, Invocation<SENDER> invocation, MATCHER matcher) {
        List<CompletableFuture<ScheduledRequirement<?>>> requirements = new ArrayList<>();

        for (Argument<?> argument : executor.getArguments()) {
            requirements.add(scheduler.supply(argument.meta().get(Meta.POLL_TYPE), () -> new ScheduledRequirement<>(argument, matchArgument(argument, invocation, matcher))));
        }

        for (ContextRequirement<?> contextRequirement : executor.getContextRequirements()) {
            requirements.add(scheduler.supply(contextRequirement.meta().get(Meta.POLL_TYPE), () -> new ScheduledRequirement<>(contextRequirement, matchContext(contextRequirement, invocation))));
        }

        for (BindRequirement<?> bindRequirement : executor.getBindRequirements()) {
            requirements.add(scheduler.supply(bindRequirement.meta().get(Meta.POLL_TYPE), () -> new ScheduledRequirement<>(bindRequirement, matchBind(bindRequirement))));
        }

        return requirements;
    }

    private static class ScheduledRequirement<T> {

        private final Requirement<T> requirement;
        private final RequirementResult<?> match;

        public ScheduledRequirement(Requirement<T> requirement, RequirementResult<?> match) {
            this.requirement = requirement;
            this.match = match;
        }

        public RequirementResult<?> getMatch() {
            return match;
        }

        public SchedulerPoll type() {
            return requirement.meta().get(Meta.POLL_TYPE);
        }

    }

    @SuppressWarnings("unchecked")
    private <PARSED, MATCHER extends ParseableInputMatcher<MATCHER>> RequirementResult<PARSED> matchArgument(Argument<PARSED> argument, Invocation<SENDER> invocation, MATCHER matcher) {
        WrapFormat<PARSED, ?> wrapFormat = argument.getWrapperFormat();
        ParserSet<SENDER, PARSED> parserSet = (ParserSet<SENDER, PARSED>) cachedParserSets.get(wrapFormat.getParsedType(), argument.getKey());

        if (parserSet == null) {
            parserSet = parserRegistry.getParserSet(wrapFormat.getParsedType(), argument.getKey());
            cachedParserSets.put(wrapFormat.getParsedType(), argument.getKey(), parserSet);
        }

        return matcher.nextArgument(invocation, argument, parserSet);
    }

    private <PARSED> RequirementResult<PARSED> matchContext(ContextRequirement<PARSED> contextRequirement, Invocation<SENDER> invocation) {
        return contextRegistry.provideContext(contextRequirement.getWrapperFormat().getParsedType(), invocation);
    }

    private <PARSED> RequirementResult<?> matchBind(BindRequirement<PARSED> bindRequirement) {
        WrapFormat<PARSED, ?> wrapFormat = bindRequirement.getWrapperFormat();
        Result<PARSED, String> instance = bindRegistry.getInstance(wrapFormat.getParsedType());

        if (instance.isOk()) {
            return ParseResult.success(instance.get());
        }

        return ParseResult.failure(instance.getError());
    }

}
