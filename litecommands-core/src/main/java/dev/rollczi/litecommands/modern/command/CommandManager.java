package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueService;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapper;
import dev.rollczi.litecommands.modern.platform.Platform;
import panda.std.Option;
import panda.std.Pair;
import panda.std.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.sun.javafx.tk.quantum.PaintCollector.collector;

public class CommandManager<SENDER> {

    private final CommandRootRoute root = new CommandRootRoute();

    private final ExpectedValueService expectedValueService;
    private final ArgumentService<SENDER> argumentService;
    private final Platform<SENDER> platform;

    public CommandManager(ExpectedValueService expectedValueService, ArgumentService<SENDER> argumentService, Platform<SENDER> platform) {
        this.expectedValueService = expectedValueService;
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
        List<Supplier<ExpectedValueWrapper<?>>> argumentsProviders = new ArrayList<>();
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

            argumentsProviders.add(() -> expectedValueService.wrap(argumentResult, argumentContext))
        }

        for (ArgumentResultContext<?, ?> resultContext : collector.getResults()) {
            Result<? extends ExpectedValueWrapper<?>, FailedReason> result = expectedValueService.wrap(resultContext);

            if (result.isErr()) {
                return; // return error
            }

            return wrapResult.get();
        }

        Result<Object, FailedReason> execute = commandExecutor.execute(collector);
    }



}
