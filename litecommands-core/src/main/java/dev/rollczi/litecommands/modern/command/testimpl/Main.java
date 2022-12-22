package dev.rollczi.litecommands.modern.command.testimpl;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.CommandExecute;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResultContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.SuccessfulResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueService;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapper;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.implementations.OptionExpectedValueWrapperFactory;
import dev.rollczi.litecommands.modern.command.argument.method.AnnotatedParameterArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverImpl;
import dev.rollczi.litecommands.modern.command.argument.method.Arg;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResultCollector;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentInvocationParser;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverRegistry;
import dev.rollczi.litecommands.modern.command.argument.method.MethodExecuteCommandService;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.litecommands.suggestion.Suggester;

import java.util.function.Supplier;

public class Main {

    static ExpectedValueService expectedValueService = new ExpectedValueService();

    static {
        expectedValueService.registerFactory(new OptionExpectedValueWrapperFactory());
    }

    public static void main(String[] args) {
        // create service and register resolver
        ArgumentService<Void> argumentService = new ArgumentService<>();
        ArgumentResolverRegistry.IndexKey<Arg, String, ArgumentContext<Arg, String>> indexKey = ArgumentResolverRegistry.IndexKey.of(Arg.class, String.class, AnnotatedParameterArgumentContext.class);

        // register resolver
        argumentService.registerResolver(indexKey, new ArgumentResolverImpl<>(new StringArgument()));


        // create context
        LiteSender liteSender = null;
        Invocation<Void> invocation = new Invocation<>(null, liteSender, "test", "test", new String[]{"test", "test"});

        MethodExecuteCommandService executeCommandService = new MethodExecuteCommandService(expectedValueService);
        CommandExecute commandExecute = executeCommandService.create(TestMethod.class.getDeclaredMethods()[0]);

        // create collector and iterate over arguments to resolve
        ArgumentResultCollector<Void> collector = ArgumentResultCollector.create(invocation);

        for (ArgumentContext<?, ?> argumentContext : commandExecute) {
            collector = argumentService.resolve(argumentContext, ArgumentKey.DEFAULT, collector);
        }

        for (ArgumentResultContext<?, ?> resultContext : collector.getResults()) {
            ExpectedValueWrapper<?> valueWrapper = resolve(resultContext);
            System.out.println(valueWrapper.getWrappedValue());
        }
    }

    private static <DETERMINANT, EXPECTED> ExpectedValueWrapper<EXPECTED> resolve(ArgumentResultContext<DETERMINANT, EXPECTED> resultContext) {
        ArgumentResult<EXPECTED> result = resultContext.getResult();
        ArgumentContext<DETERMINANT, EXPECTED> context = resultContext.getContext();

        if (result.isSuccessful()) {
            SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();
            Supplier<EXPECTED> supplier = successfulResult.getParsedArgumentProvider();

            return expectedValueService.wrap(context.getExpectedWrapperType(), context.getExpectedType(), supplier);
        }

        if (result.isFailed()) {
            System.out.println(result.getFailedReason().getReason());
        }

        return null;
    }

    public static class TestMethod {

        public void test(@Arg String arg1, @Arg String arg2) {
        }

    }

    public <S, E, T extends Suggester & ArgumentInvocationParser<S, E>> void test(T t) {}
    public <S, E, T extends ArgumentInvocationParser<S, E>> void test(T t) {}

}
