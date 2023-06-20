package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.input.ArgumentsInputMatcher;
import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.command.requirement.RequirementResult;
import dev.rollczi.litecommands.command.requirement.RequirementSuccessMatch;
import dev.rollczi.litecommands.exception.ExceptionHandleService;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.result.ResultService;
import dev.rollczi.litecommands.scheduler.ScheduledChain;
import dev.rollczi.litecommands.scheduler.ScheduledChainLink;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPollType;
import dev.rollczi.litecommands.validator.ValidatorService;
import dev.rollczi.litecommands.wrapper.Wrap;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class CommandExecuteService<SENDER> {

    private final ValidatorService<SENDER> validatorService;
    private final ResultService<SENDER> resultResolver;
    private final ExceptionHandleService<SENDER> exceptionHandleService;
    private final Scheduler scheduler;

    public CommandExecuteService(ValidatorService<SENDER> validatorService, ResultService<SENDER> resultResolver, ExceptionHandleService<SENDER> exceptionHandleService, Scheduler scheduler) {
        this.validatorService = validatorService;
        this.resultResolver = resultResolver;
        this.exceptionHandleService = exceptionHandleService;
        this.scheduler = scheduler;
    }

    public CompletableFuture<CommandExecuteResult> execute(Invocation<SENDER> invocation, ArgumentsInputMatcher<?> matcher, CommandRoute<SENDER> commandRoute) {
        return execute0(invocation, matcher, commandRoute).thenCompose(executeResult -> scheduler.supplySync(() -> {
            this.handleResult(invocation, executeResult);

            return executeResult;
        }));
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

    private <MATCHER extends ArgumentsInputMatcher<MATCHER>> CompletableFuture<CommandExecuteResult> execute0(
        Invocation<SENDER> invocation,
        ArgumentsInputMatcher<MATCHER> matcher,
        CommandRoute<SENDER> commandRoute
    ) {
        return this.execute(commandRoute.getExecutors().iterator(), invocation, matcher, commandRoute, null);
    }


    private <MATCHER extends ArgumentsInputMatcher<MATCHER>> CompletableFuture<CommandExecuteResult> execute(
        Iterator<CommandExecutor<SENDER, ?>> executors,
        Invocation<SENDER> invocation,
        ArgumentsInputMatcher<MATCHER> matcher,
        CommandRoute<SENDER> commandRoute,
        @Nullable FailedReason last
    ) {
        // Handle failed
        if (!executors.hasNext()) {
            if (last != null && !last.isEmpty()) {
                return completedFuture(CommandExecuteResult.failed(last.getReason()));
            }

            return completedFuture(CommandExecuteResult.failed(InvalidUsage.Cause.UNKNOWN_COMMAND));
        }

        CommandExecutor<SENDER, ?> executor = executors.next();

        // Handle matching arguments
        return this.match(executor, invocation, matcher.copy()).thenCompose(match -> {
            if (match.isFailed()) {
                FailedReason current = match.getFailedReason();

                if (!current.isEmpty()) {
                    return this.execute(executors, invocation, matcher, commandRoute, current);
                }

                return this.execute(executors, invocation, matcher, commandRoute, last);
            }

            // Handle validation
            Flow flow = this.validatorService.validate(invocation, commandRoute, executor);

            if (flow.isTerminate()) {
                return completedFuture(CommandExecuteResult.failed(flow.getReason()));
            }

            if (flow.isStopCurrent()) {
                return this.execute(executors, invocation, matcher, commandRoute, flow.failedReason());
            }

            // Execution
            SchedulerPollType type = executor.getMeta().get(CommandMeta.POLL_TYPE);

            return scheduler.supply(type, () -> {
                try {
                    return match.executeCommand();
                }
                catch (LiteCommandsReflectException exception) {
                    return CommandExecuteResult.thrown(exception.getCause());
                }
                catch (Throwable error) {
                    return CommandExecuteResult.thrown(error);
                }
            });
        }).exceptionally(throwable -> CommandExecuteResult.thrown(throwable));
    }

    private <REQUIREMENT extends Requirement<SENDER, ?>, MATCHER extends ArgumentsInputMatcher<MATCHER>> CompletableFuture<CommandExecutorMatchResult> match(
        CommandExecutor<SENDER, REQUIREMENT> executor,
        Invocation<SENDER> invocation,
        MATCHER matcher
    ) {
        ScheduledChain.Builder<ScheduledRequirement<REQUIREMENT>, Wrap<?>> builder = ScheduledChain.builder();

        for (REQUIREMENT requirement : executor.getRequirements()) {
            RequirementResult<?> result = requirement.match(invocation, matcher);

            if (!result.isSuccess()) {
                return completedFuture(CommandExecutorMatchResult.failed(result.getFailedReason()));
            }

            builder.link(new ScheduledRequirement<>(requirement, result));
        }

        ArgumentsInputMatcher.EndResult endResult = matcher.endMatch();

        if (!endResult.isSuccessful()) {
            return completedFuture(CommandExecutorMatchResult.failed(endResult.getFailedReason()));
        }

        return builder.build((scheduledRequirement, wrap) -> toSuccess(scheduledRequirement.requirement, wrap))
            .call(scheduler)
            .thenApply(wraps -> executor.match(wraps));
    }

    @SuppressWarnings("unchecked")
    private <R extends Requirement<SENDER, ? extends T>, T> RequirementSuccessMatch<SENDER, R, T> toSuccess(R requirement, Wrap<?> wrap) {
        return new RequirementSuccessMatch<>(requirement, (Wrap<T>) wrap);
    }

    private class ScheduledRequirement<R extends Requirement<SENDER, ?>> implements ScheduledChainLink<Wrap<?>> {
        private final R requirement;
        private final RequirementResult<?> result;

        public ScheduledRequirement(R requirement, RequirementResult<?> result) {
            this.requirement = requirement;
            this.result = result;
        }

        @Override
        public Wrap<?> call() {
            return result.getSuccess();
        }

        @Override
        public SchedulerPollType type() {
            return requirement.pollType();
        }
    }

}
