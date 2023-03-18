package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.annotation.argument.Arg;
import dev.rollczi.litecommands.modern.annotation.argument.ArgAnnotationResolver;
import dev.rollczi.litecommands.modern.annotation.context.Context;
import dev.rollczi.litecommands.modern.annotation.context.ContextAnnotationResolver;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistry;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistry.IndexKey;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistryImpl;
import dev.rollczi.litecommands.modern.argument.PreparedArgument;
import dev.rollczi.litecommands.modern.argument.type.baisc.AbstractNumberArgumentResolver;
import dev.rollczi.litecommands.modern.argument.type.baisc.StringArgumentResolver;
import dev.rollczi.litecommands.modern.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.test.FakeSender;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedService;
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
        bindRegistry.bindContextual(Invocation.class, Result::ok);
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

        assertThrows(IllegalArgumentException.class, () ->  new MethodCommandExecutorService<>().create(testCommand, method));
    }

}