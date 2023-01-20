package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.argument.invocation.CommandExpectedContextualWrapperProvider;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.extension.annotated.inject.InjectBindRegistry;
import dev.rollczi.litecommands.modern.platform.Platform;
import panda.std.Result;

import java.util.List;

public class CommandManager<SENDER> {

    private final CommandRootRouteImpl root = new CommandRootRouteImpl();

    private final WrappedExpectedContextualService wrappedExpectedContextualService;
    private final ArgumentService<SENDER> argumentService;
    private final Platform<SENDER> platform;
    private final CommandExecuteResultResolver<SENDER> resultResolver;
    private final InjectBindRegistry<SENDER> injectBindRegistry;

    public CommandManager(WrappedExpectedContextualService wrappedExpectedContextualService, ArgumentService<SENDER> argumentService, Platform<SENDER> platform, CommandExecuteResultResolver<SENDER> resultResolver, InjectBindRegistry<SENDER> injectBindRegistry) {
        this.wrappedExpectedContextualService = wrappedExpectedContextualService;
        this.argumentService = argumentService;
        this.platform = platform;
        this.resultResolver = resultResolver;
        this.injectBindRegistry = injectBindRegistry;
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
            Result<CommandExecuteResult, FailedReason> result = executor.execute(invocation, new CommandExpectedContextualWrapperProvider());

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
