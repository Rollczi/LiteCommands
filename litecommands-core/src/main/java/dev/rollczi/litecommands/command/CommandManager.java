package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.ArgumentService;
import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationResult;
import dev.rollczi.litecommands.platform.LiteSettings;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.validator.CommandValidatorResult;
import dev.rollczi.litecommands.validator.CommandValidatorService;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;

import java.util.List;

public class CommandManager<SENDER, C extends LiteSettings> {

    private final CommandRootRouteImpl<SENDER> root = new CommandRootRouteImpl<>();

    private final Platform<SENDER, C> platform;

    private final CommandValidatorService<SENDER> commandValidatorService;
    private final ArgumentService<SENDER> argumentService;
    private final WrappedExpectedService wrappedArgumentService;
    private final CommandExecuteResultResolver<SENDER> resultResolver;

    public CommandManager(
        Platform<SENDER, C> platform,
        WrappedExpectedService wrappedArgumentService,
        ArgumentService<SENDER> argumentService,
        CommandExecuteResultResolver<SENDER> resultResolver,
        CommandValidatorService<SENDER> commandValidatorService
    ) {
        this.platform = platform;

        this.commandValidatorService = commandValidatorService;
        this.argumentService = argumentService;
        this.wrappedArgumentService = wrappedArgumentService;
        this.resultResolver = resultResolver;
    }

    public CommandRoute<SENDER> getRoot() {
        return this.root;
    }

    public void register(CommandRoute<SENDER> commandRoute) {
        this.platform.register(commandRoute, invocation -> {
            InvocationResult<SENDER> invocationResult = this.execute(invocation, commandRoute);

            resultResolver.resolveInvocation(invocationResult);

            return invocationResult;
        }, invocation -> SuggestionResult.of());

        this.root.appendToRoot(commandRoute);
    }

    private InvocationResult<SENDER> execute(Invocation<SENDER> invocation, CommandRoute<SENDER> start) {
        CommandRouteFindResult<SENDER> findResult = this.find(start, invocation.argumentsList(), 0);
        CommandRoute<SENDER> commandRoute = findResult.commandRoute;
        FailedReason lastFailedReason = null;

        for (CommandExecutor<SENDER> executor : commandRoute.getExecutors()) {
            CommandExecutorMatchResult match = executor.match(invocation, new CommandExecutor.Context(findResult.childIndex));

            if (match.isFailed()) {
                FailedReason current = match.getFailedReason();

                if (!current.isEmpty()) {
                    lastFailedReason = current;
                }

                continue;
            }

            CommandValidatorResult validationResult = this.commandValidatorService.validate(invocation, commandRoute, executor);

            if (validationResult.isInvalid()) {
                if (validationResult.canBeIgnored()) {
                    continue;
                }

                return InvocationResult.failed(invocation, FailedReason.of(validationResult.getInvalidResult()));
            }

            CommandExecuteResult executeResult = match.executeCommand();

            return InvocationResult.success(invocation, executeResult);
        }

        if (lastFailedReason != null && !lastFailedReason.isEmpty()) {
            Object reason = lastFailedReason.getReason();

            return InvocationResult.failed(invocation, FailedReason.of(reason));
        }

        return InvocationResult.failed(invocation, FailedReason.of(InvalidUsage.Cause.UNKNOWN_COMMAND));
    }

    private CommandRouteFindResult<SENDER> find(CommandRoute<SENDER> command, List<String> arguments, int rawArgumentsIndex) {
        int requiredSizeArguments = rawArgumentsIndex + 1;

        if (arguments.size() < requiredSizeArguments) {
            return new CommandRouteFindResult<>(command, rawArgumentsIndex);
        }

        for (CommandRoute<SENDER> child : command.getChildren()) {
            if (!child.isNameOrAlias(arguments.get(rawArgumentsIndex))) {
                continue;
            }

            return this.find(child, arguments, rawArgumentsIndex + 1);
        }

        return new CommandRouteFindResult<>(command, rawArgumentsIndex);
    }

    public void unregisterAll() {
        this.root.clearChildren();
        this.platform.unregisterAll();
    }

    private static class CommandRouteFindResult<SENDER> {

        private final CommandRoute<SENDER> commandRoute;
        private final int childIndex;

        private CommandRouteFindResult(CommandRoute<SENDER> commandRoute, int childIndex) {
            this.commandRoute = commandRoute;
            this.childIndex = childIndex;
        }

    }

}
