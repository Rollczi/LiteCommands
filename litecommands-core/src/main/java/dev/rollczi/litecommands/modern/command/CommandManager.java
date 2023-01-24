package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.ArgumentService;
import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.filter.CommandFilterService;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.platform.Platform;
import panda.std.Result;

import java.util.List;

public class CommandManager<SENDER> {

    private final CommandRootRouteImpl root = new CommandRootRouteImpl();

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
            CommandContextualConverter<SENDER> provider = new CommandContextualConverter<>(this.argumentService, this.bindRegistry, this.wrappedArgumentService);
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
