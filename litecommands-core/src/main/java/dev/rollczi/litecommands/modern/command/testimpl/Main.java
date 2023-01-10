package dev.rollczi.litecommands.modern.command.testimpl;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.api.StringArgument;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverRegistry;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueService;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapper;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.implementations.OptionExpectedValueWrapperFactory;
import dev.rollczi.litecommands.modern.command.method.Arg;
import dev.rollczi.litecommands.modern.command.method.MethodExecuteCommandService;
import dev.rollczi.litecommands.modern.platform.PlatformSender;
import panda.std.Result;

public class Main {

    static ExpectedValueService expectedValueService = new ExpectedValueService();

    static {
        expectedValueService.registerFactory(new OptionExpectedValueWrapperFactory());
    }

    public static void main(String[] args) {
        // create service and register resolver
        ArgumentService<Void> argumentService = new ArgumentService<>();
        ArgumentResolverRegistry.IndexKey<Object, String, ArgumentContext<Object, String>> indexKey = ArgumentResolverRegistry.IndexKey.universal(String.class);

        // register resolver
        argumentService.registerResolver(indexKey, new StringArgument<>());


        // create context
        PlatformSender platformSender = null;
        Invocation<Void> invocation = new Invocation<>(null, platformSender, "test", "test", new String[]{"test", "test"});

        MethodExecuteCommandService executeCommandService = new MethodExecuteCommandService(expectedValueService);
        CommandExecutor commandExecutor = executeCommandService.create(TestMethod.class.getDeclaredMethods()[0]);

        // create collector and iterate over arguments to resolve
        ArgumentResultCollector<Void> collector = ArgumentResultCollector.create(invocation);

        for (ArgumentContext<?, ?> argumentContext : commandExecutor) {
            collector = argumentService.resolve(argumentContext, ArgumentKey.DEFAULT, collector);

            if (collector.isLastFailed()) {
                break;
            }
        }

        for (ArgumentResultContext<?, ?> resultContext : collector.getResults()) {
            ExpectedValueWrapper<?> valueWrapper = resolve(resultContext);
            System.out.println(valueWrapper.getWrappedValue());
        }

        Result<Object, FailedReason> execute = commandExecutor.execute(collector);
    }

    private static <DETERMINANT, EXPECTED> ExpectedValueWrapper<EXPECTED> resolve(ArgumentResultContext<DETERMINANT, EXPECTED> resultContext) {
        Result<ExpectedValueWrapper<EXPECTED>, FailedReason> wrapResult = expectedValueService.wrap(resultContext);

        if (wrapResult.isErr()) {
            System.out.println(wrapResult.getError().getReason());
            return null;
        }

        return wrapResult.get();
    }

    public static class TestMethod {

        public void test(@Arg String arg1, @Arg String arg2) {
        }

    }

}
