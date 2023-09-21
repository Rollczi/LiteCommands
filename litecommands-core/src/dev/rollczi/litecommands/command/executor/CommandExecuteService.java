package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.handler.result.ResultHandleService;
import dev.rollczi.litecommands.invalidusage.InvalidUsage.Cause;
import dev.rollczi.litecommands.scheduler.ScheduledChainException;
import dev.rollczi.litecommands.schematic.Schematic;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import dev.rollczi.litecommands.schematic.SchematicInput;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.requirement.RequirementResult;
import dev.rollczi.litecommands.requirement.RequirementMatch;
import dev.rollczi.litecommands.handler.exception.ExceptionHandleService;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.ScheduledChain;
import dev.rollczi.litecommands.scheduler.ScheduledChainLink;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPollType;
import dev.rollczi.litecommands.validator.ValidatorService;
import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ListIterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class CommandExecuteService<SENDER> {

    private final ValidatorService<SENDER> validatorService;
    private final ResultHandleService<SENDER> resultResolver;
    private final ExceptionHandleService<SENDER> exceptionHandleService;
    private final Scheduler scheduler;
    private final SchematicGenerator<SENDER> schematicGenerator;
    private final ParserRegistry<SENDER> parserRegistry;
    private final ContextRegistry<SENDER> contextRegistry;
    private final WrapperRegistry wrapperRegistry;

    public CommandExecuteService(ValidatorService<SENDER> validatorService, ResultHandleService<SENDER> resultResolver, ExceptionHandleService<SENDER> exceptionHandleService, Scheduler scheduler, SchematicGenerator<SENDER> schematicGenerator, ParserRegistry<SENDER> parserRegistry, ContextRegistry<SENDER> contextRegistry, WrapperRegistry wrapperRegistry) {
        this.validatorService = validatorService;
        this.resultResolver = resultResolver;
        this.exceptionHandleService = exceptionHandleService;
        this.scheduler = scheduler;
        this.schematicGenerator = schematicGenerator;
        this.parserRegistry = parserRegistry;
        this.contextRegistry = contextRegistry;
        this.wrapperRegistry = wrapperRegistry;
    }

    public CompletableFuture<CommandExecuteResult> execute(Invocation<SENDER> invocation, ParseableInputMatcher<?> matcher, CommandRoute<SENDER> commandRoute) {
        return execute0(invocation, matcher, commandRoute)
            .thenApply(commandExecuteResult -> mapResult(commandRoute, commandExecuteResult, invocation))
            .thenCompose(executeResult -> scheduler.supplySync(() -> {
                this.handleResult(invocation, executeResult);

                return executeResult;
            }))
            .exceptionally(new LastExceptionHandler<>(exceptionHandleService, invocation));
    }

    private void handleResult(Invocation<SENDER> invocation, CommandExecuteResult executeResult) {
        Throwable throwable = executeResult.getThrowable();
        if (throwable != null) {
            exceptionHandleService.resolve(invocation, throwable);
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

    @SuppressWarnings("unchecked") // TODO Support mapping of result in result resolver
    private CommandExecuteResult mapResult(CommandRoute<SENDER> commandRoute, CommandExecuteResult executeResult, Invocation<SENDER> invocation) {
        Throwable throwable = executeResult.getThrowable();
        if (throwable != null) {
            return executeResult;
        }

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
            SchedulerPollType type = executor.meta().get(Meta.POLL_TYPE);

            return scheduler.supply(type, () -> {
                try {
                    return match.executeCommand();
                } catch (LiteCommandsException exception) {
                    return CommandExecuteResult.thrown(executor, exception.getCause());
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
        ScheduledChain.Builder<ScheduledRequirement<?>, RequirementResult<?>> builder = ScheduledChain.builder();

        for (Argument<?> argument : executor.getArguments()) {
            builder.link(new ScheduledRequirement<>(argument, () -> matchArgument(argument, invocation, matcher)));
        }

        for (ContextRequirement<?> contextRequirement : executor.getContextRequirements()) {
            builder.link(new ScheduledRequirement<>(contextRequirement, () -> matchContext(contextRequirement, invocation)));
        }

        return builder.build((scheduledRequirement, requirementResult) -> {
                if (requirementResult.isFailure()) {
                    throw new ScheduledChainException(requirementResult.getError());
                }

                return toMatch(scheduledRequirement.requirement, requirementResult.getSuccess());
            })
            .collectChain(scheduler)
            .thenCompose(result -> {
                if (result.isFailure()) {
                    return completedFuture(CommandExecutorMatchResult.failed(result.getFailure()));
                }

                ParseableInputMatcher.EndResult endResult = matcher.endMatch();

                if (!endResult.isSuccessful()) {
                    return completedFuture(CommandExecutorMatchResult.failed(endResult.getFailedReason()));
                }

                RequirementsResult.Builder<SENDER> restulrBuilder = RequirementsResult.builder(invocation);

                for (RequirementMatch<? extends Requirement<?>, ?> success : result.getSuccess()) {
                    restulrBuilder.add(success.getRequirement().getName(), success);
                }

                return completedFuture(executor.match(restulrBuilder.build()));
            });
    }

    @SuppressWarnings("unchecked")
    private <R extends Requirement<? extends T>, T> RequirementMatch<R, T> toMatch(R requirement, Wrap<?> wrap) {
        return new RequirementMatch<>(requirement, (Wrap<T>) wrap);
    }

    private static class ScheduledRequirement<T> implements ScheduledChainLink<RequirementResult<?>> {

        private final Requirement<T> requirement;
        private final Supplier<RequirementResult<?>> match;

        public ScheduledRequirement(Requirement<T> requirement, Supplier<RequirementResult<?>> match) {
            this.requirement = requirement;
            this.match = match;
        }

        @Override
        public RequirementResult<?> call() {
            return match.get();
        }

        @Override
        public SchedulerPollType type() {
            return requirement.meta().get(Meta.POLL_TYPE);
        }
    }

    private <PARSED, MATCHER extends ParseableInputMatcher<MATCHER>> RequirementResult<PARSED> matchArgument(Argument<PARSED> argument, Invocation<SENDER> invocation, MATCHER matcher) {
        WrapFormat<PARSED, ?> wrapFormat = argument.getWrapperFormat();
        ParserSet<SENDER, PARSED> parserSet = parserRegistry.getParserSet(wrapFormat.getParsedType(), argument.toKey());
        ParseResult<PARSED> result = matcher.nextArgument(invocation, argument, parserSet);
        Wrapper wrapper = wrapperRegistry.getWrappedExpectedFactory(wrapFormat);

        if (result.isSuccessful()) {
            PARSED successfulResult = result.getSuccessfulResult();

            return RequirementResult.success(wrapper.create(successfulResult, wrapFormat));
        }

        FailedReason failedReason = result.getFailedReason();

        if (failedReason.getReason() == InvalidUsage.Cause.MISSING_ARGUMENT && wrapper.canCreateEmpty()) {
            return RequirementResult.success(wrapper.createEmpty(wrapFormat));
        }

        return RequirementResult.failure(failedReason);
    }


    private <PARSED> RequirementResult<PARSED> matchContext(ContextRequirement<PARSED> contextRequirement, Invocation<SENDER> invocation) {
        WrapFormat<PARSED, ?> wrapFormat = contextRequirement.getWrapperFormat();
        ContextResult<PARSED> result = contextRegistry.provideContext(wrapFormat.getParsedType(), invocation);
        Wrapper wrapper = wrapperRegistry.getWrappedExpectedFactory(wrapFormat);

        if (result.hasResult()) {
            return RequirementResult.success(wrapper.create(result.getResult(), wrapFormat));
        }

        return RequirementResult.failure(result.getError());
    }

}
