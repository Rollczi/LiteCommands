package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.event.CandidateExecutorFoundEvent;
import dev.rollczi.litecommands.command.executor.event.CandidateExecutorMatchEvent;
import dev.rollczi.litecommands.command.executor.event.CommandPostExecutionEvent;
import dev.rollczi.litecommands.command.executor.event.CommandPreExecutionEvent;
import dev.rollczi.litecommands.command.executor.flow.ExecuteFlow;
import dev.rollczi.litecommands.invalidusage.InvalidUsageException;
import dev.rollczi.litecommands.event.EventPublisher;
import dev.rollczi.litecommands.handler.result.ResultHandleService;
import dev.rollczi.litecommands.requirement.RequirementMatchService;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.validator.ValidatorService;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class CommandExecuteService<SENDER> {

    private final ValidatorService<SENDER> validatorService;
    private final ResultHandleService<SENDER> resultResolver;
    private final Scheduler scheduler;
    private final RequirementMatchService<SENDER> requirementMatchService;
    private final EventPublisher publisher;

    public CommandExecuteService(
        ValidatorService<SENDER> validatorService,
        ResultHandleService<SENDER> resultResolver,
        Scheduler scheduler,
        RequirementMatchService<SENDER> requirementMatchService,
        EventPublisher publisher
    ) {
        this.validatorService = validatorService;
        this.resultResolver = resultResolver;
        this.scheduler = scheduler;
        this.requirementMatchService = requirementMatchService;
        this.publisher = publisher;
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

    //TODO: Move to Event subscriber (CommandPostExecutionEvent, priority = MAX)
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
            } // TODO: event (CommandExcutorNotFoundEvent)

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
                return this.execute(executors, invocation, matcher, commandRoute, FailedReason.max(foundEvent.getFlowResult(), last));
            }
        }

        // Handle matching arguments
        return this.requirementMatchService.match(executor, invocation, matcher.copy()).thenCompose(match -> {
            if (publisher.hasSubscribers(CandidateExecutorMatchEvent.class)) {
                CandidateExecutorMatchEvent<SENDER> matchEvent = publisher.publish(new CandidateExecutorMatchEvent<>(invocation, executor, match));
                if (matchEvent.getFlow() == ExecuteFlow.STOP) {
                    return completedFuture(CommandExecuteResult.failed(executor, matchEvent.getFlowResult()));
                }

                if (matchEvent.getFlow() == ExecuteFlow.SKIP) {
                    return this.execute(executors, invocation, matcher, commandRoute, FailedReason.max(matchEvent.getFlowResult(), last));
                }
            }

            if (match.isFailed()) {
                FailedReason current = match.getFailedReason();

                if (current.hasResult()) {
                    return this.execute(executors, invocation, matcher, commandRoute, FailedReason.max(current, last));
                }

                return this.execute(executors, invocation, matcher, commandRoute, last);
            }

            if (publisher.hasSubscribers(CommandPreExecutionEvent.class)) {
                CommandPreExecutionEvent<SENDER> executionEvent = publisher.publish(new CommandPreExecutionEvent<>(invocation, executor));
                if (executionEvent.getFlow() == ExecuteFlow.STOP) {
                    return completedFuture(CommandExecuteResult.failed(executor, executionEvent.getFlowResult()));
                }

                if (executionEvent.getFlow() == ExecuteFlow.SKIP) {
                    return this.execute(executors, invocation, matcher, commandRoute, FailedReason.max(executionEvent.getFlowResult(), last));
                }
            }

            // Execution
            SchedulerPoll type = executor.metaCollector().findFirst(Meta.POLL_TYPE);

            return scheduler.supply(type, () -> execute(match, executor));
        }).exceptionally(throwable -> toThrown(executor, throwable));
    }

    private CommandExecuteResult toThrown(CommandExecutor<SENDER> executor, Throwable throwable) {
        if (throwable instanceof CompletionException) {
            return CommandExecuteResult.thrown(executor, throwable.getCause());
        }

        return CommandExecuteResult.thrown(executor, throwable);
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

}
