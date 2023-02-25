package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.ArgumentService;
import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.filter.CommandFilterService;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.invocation.InvocationResult;
import dev.rollczi.litecommands.modern.platform.Platform;
import panda.std.Result;

import java.util.List;

public class CommandManager<SENDER> {

    private final CommandRootRouteImpl<SENDER> root = new CommandRootRouteImpl<>();

    private final Platform<SENDER> platform;

    private final CommandFilterService<SENDER> commandFilterService;
    private final ArgumentService<SENDER> argumentService;
    private final BindRegistry<SENDER> bindRegistry;
    private final WrappedExpectedContextualService wrappedArgumentService;
    private final CommandExecuteResultResolver<SENDER> resultResolver;

    public CommandManager(
        Platform<SENDER> platform,
        WrappedExpectedContextualService wrappedArgumentService,
        ArgumentService<SENDER> argumentService,
        CommandExecuteResultResolver<SENDER> resultResolver,
        BindRegistry<SENDER> bindRegistry,
        CommandFilterService<SENDER> commandFilterService
    ) {
        this.platform = platform;

        this.commandFilterService = commandFilterService;
        this.argumentService = argumentService;
        this.bindRegistry = bindRegistry;
        this.wrappedArgumentService = wrappedArgumentService;
        this.resultResolver = resultResolver;
    }

    public CommandRoute<SENDER> getRoot() {
        return this.root;
    }

    public void registerCommand(CommandRoute<SENDER> commandRoute) {
        this.platform.registerExecuteListener(commandRoute, invocation -> this.execute(invocation, commandRoute));
        this.root.appendToRoot(commandRoute);
    }

    private InvocationResult<SENDER> execute(Invocation<SENDER> invocation, CommandRoute<SENDER> start) {
        CommandRouteFindResult<SENDER> findResult = this.find(start, invocation.argumentsList(), 0);
        CommandRoute<SENDER> commandRoute = findResult.commandRoute;
        FailedReason lastFailedReason = null;

        for (CommandExecutor<SENDER> executor : commandRoute.getExecutors()) {
            if (!this.commandFilterService.isValid(invocation, commandRoute, executor)) {
                continue;
            }

            CommandContextualConverter<SENDER> provider = new CommandContextualConverter<>(this.argumentService, this.bindRegistry, this.wrappedArgumentService, findResult.childIndex);
            Result<CommandExecuteResult, FailedReason> result = executor.execute(invocation, provider);

            if (result.isErr()) {
                lastFailedReason = result.getError();
                continue;
            }

            CommandExecuteResult executeResult = result.get();

            this.resultResolver.resolve(invocation, executeResult);

            return InvocationResult.success(invocation, executeResult);
        }

        if (lastFailedReason != null && !lastFailedReason.isEmpty()) {
            Object reason = lastFailedReason.getReason();

            return InvocationResult.failed(invocation, FailedReason.of(reason));
        }

        return InvocationResult.failed(invocation, FailedReason.empty());
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

    private static class CommandRouteFindResult<SENDER> {
        private final CommandRoute<SENDER> commandRoute;
        private final int childIndex;

        private CommandRouteFindResult(CommandRoute<SENDER> commandRoute, int childIndex) {
            this.commandRoute = commandRoute;
            this.childIndex = childIndex;
        }
    }

}
