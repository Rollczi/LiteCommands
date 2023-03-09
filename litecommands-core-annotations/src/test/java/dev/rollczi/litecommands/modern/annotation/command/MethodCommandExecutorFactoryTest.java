package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.annotation.argument.Arg;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistry;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistry.IndexKey;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistryImpl;
import dev.rollczi.litecommands.modern.argument.PreparedArgument;
import dev.rollczi.litecommands.modern.argument.type.baisc.AbstractNumberArgumentResolver;
import dev.rollczi.litecommands.modern.argument.type.baisc.StringArgumentResolver;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.test.FakeSender;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MethodCommandExecutorFactoryTest {

    static class TestCommand {
        @Execute
        void execute(Invocation<FakeSender> invocation, @Arg String text, @Arg int test) {
        }
    }

    @Test
    void testCreateMethodOfExecuteFactory() {
        TestCommand testCommand = new TestCommand();
        Method method = testCommand.getClass().getDeclaredMethods()[0];

        WrappedExpectedService wrappedExpectedService = new WrappedExpectedService();
        ArgumentResolverRegistry<FakeSender> resolverRegistry = new ArgumentResolverRegistryImpl<>();
        MethodCommandExecutorFactory<FakeSender> executorFactory = MethodCommandExecutorFactory.create(wrappedExpectedService, resolverRegistry);

        assertThrows(IllegalStateException.class, () -> executorFactory.create(testCommand, method));

        resolverRegistry.registerResolver(IndexKey.universal(String.class), new StringArgumentResolver<>());
        resolverRegistry.registerResolver(IndexKey.universal(int.class), AbstractNumberArgumentResolver.ofInteger());

        CommandExecutor<FakeSender> commandExecutor = executorFactory.create(testCommand, method);

        List<WrapperFormat<?>> contextuals = commandExecutor.getContextuals();
        assertEquals(1, contextuals.size());

        List<PreparedArgument<FakeSender, ?>> arguments = commandExecutor.getArguments();
        assertEquals(2, arguments.size());

        CommandMeta meta = commandExecutor.getMeta();
        assertEquals(0, meta.getKeys().size());
    }

}