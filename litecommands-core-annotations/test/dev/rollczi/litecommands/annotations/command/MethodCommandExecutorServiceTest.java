package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.ArgAnnotationResolver;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.context.ContextAnnotationResolver;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserRegistryImpl;
import dev.rollczi.litecommands.argument.resolver.std.NumberArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.std.StringArgumentResolver;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MethodCommandExecutorServiceTest {

    static class TestCommand {
        @Execute
        void execute(@Context Invocation<TestSender> invocation, @Arg String text, @Arg int test) {
        }
    }

    static ContextRegistry<TestSender> bindRegistry = new ContextRegistry<>();
    static ParserRegistry<TestSender> resolverRegistry = new ParserRegistryImpl<>();
    static MethodCommandExecutorService<TestSender> executorFactory = new MethodCommandExecutorService<>();
    static WrapperRegistry expectedService = new WrapperRegistry();

    @BeforeAll
    static void beforeAll() {
        bindRegistry.registerProvider(Invocation.class, invocation -> ContextResult.ok(() -> invocation)); // Do not use short method reference here (it will cause bad return type in method reference on Java 8)
        resolverRegistry.registerParser(String.class, ArgumentKey.of(), new StringArgumentResolver<>());
        resolverRegistry.registerParser(int.class, ArgumentKey.of(), NumberArgumentResolver.ofInteger());

        executorFactory.registerResolver(Context.class, new ContextAnnotationResolver<>(bindRegistry, expectedService));
        executorFactory.registerResolver(Arg.class, new ArgAnnotationResolver<>(expectedService, resolverRegistry));
    }

    @Test
    void testCreateMethodOfExecuteFactory() {
        TestCommand testCommand = new TestCommand();
        Method method = testCommand.getClass().getDeclaredMethods()[0];

        CommandExecutor<TestSender, ?> commandExecutor = executorFactory.create(testCommand, method);

        List<Requirement<?, ?>> requirements = (List<Requirement<?, ?>>) commandExecutor.getRequirements();
        assertEquals(3, requirements.size());

        Meta meta = commandExecutor.meta();
        assertEquals(0, meta.getKeys().size());
    }

    @Test
    void testThrowCreate() {
        TestCommand testCommand = new TestCommand();
        Method method = testCommand.getClass().getDeclaredMethods()[0];

        assertThrows(LiteCommandsReflectException.class, () -> new MethodCommandExecutorService<>().create(testCommand, method));
    }

}