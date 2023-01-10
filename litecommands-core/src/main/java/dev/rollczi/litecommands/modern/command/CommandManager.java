package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.WrappedArgumentService;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.WrappedArgumentWrapper;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.WrappedArgumentSet;
import dev.rollczi.litecommands.modern.platform.Platform;
import panda.std.Option;
import panda.std.Result;

import java.util.List;

public class CommandManager<SENDER> {

    private final CommandRootRouteImpl root = new CommandRootRouteImpl();

    private final WrappedArgumentService wrappedArgumentService;
    private final ArgumentService<SENDER> argumentService;
    private final Platform<SENDER> platform;

    public CommandManager(WrappedArgumentService wrappedArgumentService, ArgumentService<SENDER> argumentService, Platform<SENDER> platform) {
        this.wrappedArgumentService = wrappedArgumentService;
        this.argumentService = argumentService;
        this.platform = platform;
    }

    public CommandRoute getRoot() {
        return root;
    }

    public void registerCommand(CommandRoute commandRoute) {
        platform.registerCommandExecuteListener();
    }

    private void find(Invocation<SENDER> invocation, CommandRoute route) {


        find(route, invocation.argumentsList(), 0);
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

            return find(child, arguments, rawArgumentsIndex + 1);
        }

        return command;
    }

    private Result<Object, FailedReason> execute(CommandRoute command, ) {

    }

    private Result<CommandExecuteResult, FailedReason> execute(Invocation<SENDER> invocation, CommandExecutor executor) {
        WrappedArgumentSet wrappedArgumentSet = new WrappedArgumentSet();
        ArgumentResolverContext resolverContext = ArgumentResolverContext.create();

        for (ArgumentContext<?, ?> argumentContext : executor) {
            resolverContext = argumentService.resolve(invocation, argumentContext, ArgumentKey.DEFAULT, resolverContext);
            Option<ArgumentResult<?>> result = resolverContext.getLastArgumentResult();

            if (result.isEmpty()) {
                throw new IllegalStateException();
            }

            ArgumentResult<?> argumentResult = result.get();

            if (argumentResult.isFailed()) {
                return Result.error(argumentResult.getFailedReason());
            }

            wrappedArgumentSet.add(() -> wrap(argumentResult, argumentContext));
        }

        return Result.ok(executor.execute(invocation, wrappedArgumentSet));
    }

    @SuppressWarnings("unchecked")
    private <T> WrappedArgumentWrapper<T> wrap(ArgumentResult<T> argumentResult, ArgumentContext<?, ?> argumentContext) {
        return wrappedArgumentService.wrap(argumentResult, (ArgumentContext<?, T>) argumentContext);
    }

}
