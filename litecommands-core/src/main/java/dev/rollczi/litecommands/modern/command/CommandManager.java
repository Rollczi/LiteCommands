package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.argument.invocation.SuccessfulResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.WrappedArgumentProvider;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentService;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentWrapper;
import dev.rollczi.litecommands.modern.platform.Platform;
import panda.std.Option;
import panda.std.Result;

import java.util.List;
import java.util.function.Supplier;

public class CommandManager<SENDER> {

    private final CommandRootRouteImpl root = new CommandRootRouteImpl();

    private final WrappedArgumentService wrappedArgumentService;
    private final ArgumentService<SENDER> argumentService;
    private final Platform<SENDER> platform;
    private final CommandExecuteResultResolver<SENDER> resultResolver;

    public CommandManager(WrappedArgumentService wrappedArgumentService, ArgumentService<SENDER> argumentService, Platform<SENDER> platform, CommandExecuteResultResolver<SENDER> resultResolver) {
        this.wrappedArgumentService = wrappedArgumentService;
        this.argumentService = argumentService;
        this.platform = platform;
        this.resultResolver = resultResolver;
    }

    public CommandRoute getRoot() {
        return root;
    }

    public void registerCommand(CommandRoute commandRoute) {
        platform.registerCommandExecuteListener(invocation -> {});
    }

    private void execute(Invocation<SENDER> invocation) {
        CommandRoute commandRoute = find(root, invocation.argumentsList(), 0);
        FailedReason lastFailedReason = null;

        for (CommandExecutor executor : commandRoute.getExecutors()) {
            Result<CommandExecuteResult, FailedReason> result = executor.execute(invocation, new CommandWrappedArgumentProvider());

            if (result.isErr()) {
                lastFailedReason = result.getError();
                continue;
            }

            CommandExecuteResult executeResult = result.get();

            resultResolver.resolve(invocation, executeResult);
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

            return find(child, arguments, rawArgumentsIndex + 1);
        }

        return command;
    }

    public class CommandWrappedArgumentProvider implements WrappedArgumentProvider<SENDER> {

        private ArgumentResolverContext<?> resolverContext = ArgumentResolverContext.create();

        @Override
        public <EXPECTED> Result<Supplier<WrappedArgumentWrapper<EXPECTED>>, FailedReason> provide(Invocation<SENDER> invocation, ExpectedContextual<EXPECTED> expectedContextual) {
            if (expectedContextual instanceof ArgumentContextual) {
                ArgumentContextual<?, EXPECTED> argumentContextual = (ArgumentContextual<?, EXPECTED>) expectedContextual;

                return this.provideArgumentContextual(invocation, argumentContextual);
            }

            return this.provideContextual(invocation, expectedContextual);
        }

        private <EXPECTED> Result<Supplier<WrappedArgumentWrapper<EXPECTED>>, FailedReason> provideArgumentContextual(Invocation<SENDER> invocation, ArgumentContextual<?, EXPECTED> argumentContextual) {
            ArgumentResolverContext<EXPECTED> current = argumentService.resolve(invocation, argumentContextual, ArgumentKey.DEFAULT, resolverContext);
            this.resolverContext = current;

            Option<ArgumentResult<EXPECTED>> result = current.getLastArgumentResult();

            if (result.isEmpty()) {
                throw new IllegalStateException();
            }

            ArgumentResult<EXPECTED> argumentResult = result.get();

            if (argumentResult.isFailed()) {
                Option<WrappedArgumentWrapper<EXPECTED>> wrapper = wrappedArgumentService.empty(argumentContextual);

                if (wrapper.isEmpty()) {
                    return Result.error(argumentResult.getFailedReason());
                }

                return Result.ok(wrapper::get);
            }

            SuccessfulResult<EXPECTED> successfulResult = argumentResult.getSuccessfulResult();

            return Result.ok(() -> wrappedArgumentService.wrap(successfulResult.getExpectedContextualProvider(), argumentContextual));
        }

        private <EXPECTED> Result<Supplier<WrappedArgumentWrapper<EXPECTED>>, FailedReason> provideContextual(Invocation<SENDER> invocation, ExpectedContextual<EXPECTED> expectedContextual) {

            return Result.error(FailedReason.of("Not supported")); //TODO search beans and contextual beans
        }

    }
}
