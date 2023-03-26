package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.ArgAnnotationResolver;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.context.ContextAnnotationResolver;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.ArgumentResolverRegistry;
import dev.rollczi.litecommands.argument.ArgumentResolverRegistry.IndexKey;
import dev.rollczi.litecommands.argument.ArgumentResolverRegistryImpl;
import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.argument.type.baisc.AbstractNumberArgumentResolver;
import dev.rollczi.litecommands.argument.type.baisc.StringArgumentResolver;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.test.FakeSender;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import panda.std.Result;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MethodCommandExecutorServiceTest {

    static class TestCommand {
        @Execute
        void execute(@Context Invocation<FakeSender> invocation, @Arg String text, @Arg int test) {
        }
    }

    static BindRegistry<FakeSender> bindRegistry = new BindRegistry<>();
    static ArgumentResolverRegistry<FakeSender> resolverRegistry = new ArgumentResolverRegistryImpl<>();
    static MethodCommandExecutorService<FakeSender> executorFactory = new MethodCommandExecutorService<>();
    static WrappedExpectedService expectedService = new WrappedExpectedService();

    @BeforeAll
    static void beforeAll() {
        bindRegistry.bindContextual(Invocation.class, invocation -> Result.ok(invocation)); // Do not use short method reference here (it will cause bad return type in method reference on Java 8)
        resolverRegistry.registerResolver(IndexKey.universal(String.class), new StringArgumentResolver<>());
        resolverRegistry.registerResolver(IndexKey.universal(int.class), AbstractNumberArgumentResolver.ofInteger());

        executorFactory.registerResolver(Context.class, new ContextAnnotationResolver<>(bindRegistry, expectedService));
        executorFactory.registerResolver(Arg.class, new ArgAnnotationResolver<>(expectedService, resolverRegistry));
    }

    @Test
    void testCreateMethodOfExecuteFactory() {
        TestCommand testCommand = new TestCommand();
        Method method = testCommand.getClass().getDeclaredMethods()[0];

        CommandExecutor<FakeSender> commandExecutor = executorFactory.create(testCommand, method);

        List<PreparedArgument<FakeSender, ?>> arguments = commandExecutor.getArguments();
        assertEquals(3, arguments.size());

        CommandMeta meta = commandExecutor.getMeta();
        assertEquals(0, meta.getKeys().size());
    }

    @Test
    void testThrowCreate() {
        TestCommand testCommand = new TestCommand();
        Method method = testCommand.getClass().getDeclaredMethods()[0];

        assertThrows(IllegalArgumentException.class, () -> new MethodCommandExecutorService<>().create(testCommand, method));
    }

}