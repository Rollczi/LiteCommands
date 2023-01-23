package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.platform.Platform;
import panda.std.Result;

import java.util.List;

public class CommandManager<SENDER> {

    private final CommandRootRouteImpl root = new CommandRootRouteImpl();

    private final Platform<SENDER> platform;

    private final WrappedExpectedContextualService wrappedArgumentService;
    private final ArgumentService<SENDER> argumentService;
    private final CommandExecuteResultResolver<SENDER> resultResolver;
    private final BindRegistry<SENDER> bindRegistry;

    public CommandManager(WrappedExpectedContextualService wrappedArgumentService, ArgumentService<SENDER> argumentService, Platform<SENDER> platform, CommandExecuteResultResolver<SENDER> resultResolver, BindRegistry<SENDER> bindRegistry) {
        this.wrappedArgumentService = wrappedArgumentService;
        this.argumentService = argumentService;
        this.platform = platform;
        this.resultResolver = resultResolver;
        this.bindRegistry = bindRegistry;
    }

    public CommandRoute getRoot() {
        return this.root;
    }

    public void registerCommand(CommandRoute commandRoute) {
        this.platform.registerCommandExecuteListener(invocation -> {});
    }

    private void execute(Invocation<SENDER> invocation) {
        CommandRoute commandRoute = this.find(this.root, invocation.argumentsList(), 0);
        FailedReason lastFailedReason = null;

        for (CommandExecutor executor : commandRoute.getExecutors()) {
            CommandWrappedExpectedContextualProvider<SENDER> provider = new CommandWrappedExpectedContextualProvider<>(this.argumentService, this.wrappedArgumentService, this.bindRegistry);
            Result<CommandExecuteResult, FailedReason> result = executor.execute(invocation, provider);

            if (result.isErr()) {
                lastFailedReason = result.getError();
                continue;
            }

            CommandExecuteResult executeResult = result.get();

            this.resultResolver.resolve(invocation, executeResult);
            return;
        }

        if (lastFailedReason != null) {
            Object reason = lastFailedReason.getReason();


        }
    }

    private CommandRoute find(CommandRoute command, List<String> arguments, int rawArgumentsIndex) {
        int requiredSizeArguments = rawArgumentsIndex + 1;

        if (arguments.size() < requiredSizeArguments) {
            return command;
        }

        for (CommandRoute child : command.getChildren()) {
            if (!child.isNameOrAlias(arguments.get(rawArgumentsIndex))) {
                continue;
            }

            return this.find(child, arguments, rawArgumentsIndex + 1);
        }

        return command;
    }

}
