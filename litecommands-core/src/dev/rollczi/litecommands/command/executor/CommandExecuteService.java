package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.event.CommandExecutorFoundEvent;
import dev.rollczi.litecommands.command.executor.event.CommandExecutorNotFoundEvent;
import dev.rollczi.litecommands.command.executor.event.CommandPostExecutionEvent;
import dev.rollczi.litecommands.command.executor.event.CommandPreExecutionEvent;
import dev.rollczi.litecommands.event.EventPublisher;
import dev.rollczi.litecommands.handler.result.ResultHandleService;
import dev.rollczi.litecommands.invalidusage.InvalidUsage.Cause;
import dev.rollczi.litecommands.invalidusage.InvalidUsageException;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.priority.MutablePrioritizedList;
import dev.rollczi.litecommands.priority.PriorityLevel;
import dev.rollczi.litecommands.requirement.RequirementMatchService;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerType;
import dev.rollczi.litecommands.shared.FailedReason;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.completedFuture;
import java.util.concurrent.CompletionException;

public class CommandExecuteService<SENDER> {

    private final ResultHandleService<SENDER> resultResolver;
    private final Scheduler scheduler;
    private final RequirementMatchService<SENDER> requirementMatchService;
    private final EventPublisher publisher;

    public CommandExecuteService(
        ResultHandleService<SENDER> resultResolver,
        Scheduler scheduler,
        RequirementMatchService<SENDER> requirementMatchService,
        EventPublisher publisher
    ) {
        this.resultResolver = resultResolver;
        this.scheduler = scheduler;
        this.requirementMatchService = requirementMatchService;
        this.publisher = publisher;
    }

    public CompletableFuture<CommandExecuteResult> execute(Invocation<SENDER> invocation, ParseableInputMatcher<?> matcher, CommandRoute<SENDER> commandRoute) {
        return execute0(invocation, matcher, commandRoute)
            .thenApply(result -> publishAndApplyEvent(invocation, commandRoute, result))
            .thenCompose(executeResult -> scheduler.supply(SchedulerType.MAIN, () -> this.handleResult(invocation, executeResult)))
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
        return this.execute(commandRoute.getExecutors().iterator(), invocation, matcher, commandRoute, new MutablePrioritizedList<>());
    }

    private <MATCHER extends ParseableInputMatcher<MATCHER>> CompletableFuture<CommandExecuteResult> execute(
        Iterator<CommandExecutor<SENDER>> executors,
        Invocation<SENDER> invocation,
        ParseableInputMatcher<MATCHER> matcher,
        CommandRoute<SENDER> commandRoute,
        MutablePrioritizedList<FailedReason> failedReasons
    ) {
        // Handle failed
        if (!executors.hasNext()) {
            if (commandRoute.getExecutors().isEmpty()) {
                failedReasons.add(FailedReason.of(Cause.UNKNOWN_COMMAND, PriorityLevel.LOW));
            }

            CommandExecutorNotFoundEvent event = publisher.publish(new CommandExecutorNotFoundEvent(invocation, commandRoute, failedReasons));

            return completedFuture(CommandExecuteResult.failed(event.getFailedReason().getReason()));
        }

        CommandExecutor<SENDER> executor = executors.next();

        // Handle matching arguments
        return this.requirementMatchService.match(executor, invocation, matcher.copy()).thenCompose(match -> {
            CommandExecutorFoundEvent<SENDER> matchEvent = publisher.publish(new CommandExecutorFoundEvent<>(invocation, executor, match));
            if (matchEvent.isCancelled()) {
                return completedFuture(CommandExecuteResult.failed(executor, matchEvent.getCancelReason()));
            }

            CommandExecutorMatchResult processedMatch = matchEvent.getResult();
            if (processedMatch.isFailed()) {
                failedReasons.add(processedMatch.getFailedReason());
                return this.execute(executors, invocation, matcher, commandRoute, failedReasons);
            }

            CommandPreExecutionEvent<SENDER> executionEvent = publisher.publish(new CommandPreExecutionEvent<>(invocation, executor));
            if (executionEvent.isCancelled()) {
                return completedFuture(CommandExecuteResult.failed(executor, executionEvent.getCancelReason()));
            }

            // Execution

            return scheduler.supply(executionEvent.getSchedulerType(), () -> execute(processedMatch, executor));
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
