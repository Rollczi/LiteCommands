package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.argument.input.InputArgumentsMatcher;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationResult;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.result.ResultService;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.validator.ValidatorService;

public class CommandManager<SENDER, C extends PlatformSettings> {

    private final CommandRootRouteImpl<SENDER> root = new CommandRootRouteImpl<>();

    private final Platform<SENDER, C> platform;

    private final ValidatorService<SENDER> validatorService;
    private final ResultService<SENDER> resultResolver;
    private final Scheduler scheduler;

    public CommandManager(
        Platform<SENDER, C> platform,
        ResultService<SENDER> resultResolver,
        ValidatorService<SENDER> validatorService,
        Scheduler scheduler
    ) {
        this.platform = platform;
        this.validatorService = validatorService;
        this.resultResolver = resultResolver;
        this.scheduler = scheduler;
    }

    public CommandRoute<SENDER> getRoot() {
        return this.root;
    }

    public void register(CommandRoute<SENDER> commandRoute) {
        this.platform.register(commandRoute, invocation -> {
            InputArguments<?> inputArguments = invocation.arguments();
            InvocationResult<SENDER> invocationResult = this.execute(invocation, inputArguments, commandRoute);

            resultResolver.resolveInvocation(invocationResult);

            return invocationResult;
        }, invocation -> SuggestionResult.of());

        this.root.appendToRoot(commandRoute);
    }

    private <MATCHER extends InputArgumentsMatcher<MATCHER>> InvocationResult<SENDER> execute(
        Invocation<SENDER> invocation,
        InputArguments<MATCHER> inputArguments,
        CommandRoute<SENDER> start
    ) {
        MATCHER matcher = inputArguments.createMatcher();
        CommandRoute<SENDER> commandRoute = this.findRoute(start, inputArguments, matcher);
        FailedReason lastFailedReason = null;

        for (CommandExecutor<SENDER> executor : commandRoute.getExecutors()) {
            // Handle matching arguments
            CommandExecutorMatchResult match = executor.match(invocation, inputArguments, matcher.copy());

            if (match.isFailed()) {
                FailedReason current = match.getFailedReason();

                if (!current.isEmpty()) {
                    lastFailedReason = current;
                }

                continue;
            }

            // Handle validation
            Flow flow = this.validatorService.validate(invocation, commandRoute, executor);

            if (flow.isTerminate()) {
                return InvocationResult.failed(invocation, flow.failedReason());
            }

            if (flow.isStopCurrent()) {
                lastFailedReason = flow.failedReason();
                continue;
            }

            // Execution
            CommandExecuteResult executeResult = match.executeCommand();

            return InvocationResult.success(invocation, executeResult);
        }

        // Handle failed
        if (lastFailedReason != null && !lastFailedReason.isEmpty()) {
            Object reason = lastFailedReason.getReason();

            return InvocationResult.failed(invocation, FailedReason.of(reason));
        }

        return InvocationResult.failed(invocation, FailedReason.of(InvalidUsage.Cause.UNKNOWN_COMMAND));
    }

    private <MATCHER extends InputArgumentsMatcher<MATCHER>> CommandRoute<SENDER> findRoute(CommandRoute<SENDER> command, InputArguments<MATCHER> inputArguments, MATCHER matcher) {
        if (!matcher.hasNextRoute()) {
            return command;
        }

        for (CommandRoute<SENDER> child : command.getChildren()) {
            if (!child.isNameOrAlias(matcher.showNextRoute())) {
                continue;
            }

            matcher.matchNextRoute();
            return this.findRoute(child, inputArguments, matcher);
        }

        return command;
    }

    public void unregisterAll() {
        this.root.clearChildren();
        this.platform.unregisterAll();
    }

}
