package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.requirement.RequirementFactory;
import dev.rollczi.litecommands.requirement.RequirementFactoryRegistry;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.ArgArgumentFactory;
import dev.rollczi.litecommands.context.Context;
import dev.rollczi.litecommands.command.executor.Execute;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserRegistryImpl;
import dev.rollczi.litecommands.argument.resolver.std.NumberArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.std.StringArgumentResolver;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.RequirementProcessor;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodCommandExecutorFactoryTest {

    static final ContextRegistry<TestSender> CONTEXT_REGISTRY = new ContextRegistry<>();
    static final ParserRegistry<TestSender> PARSER_REGISTRY = new ParserRegistryImpl<>();
    static final WrapperRegistry WRAPPER_REGISTRY = new WrapperRegistry();
    static final RequirementFactoryRegistry<TestSender> REQUIREMENT_FACTORY_REGISTRY = new RequirementFactoryRegistry<>();

    @BeforeAll
    static void beforeAll() {
        CONTEXT_REGISTRY.registerProvider(Invocation.class, invocation -> ContextResult.ok(() -> invocation)); // Do not use short method reference here (it will cause bad return type in method reference on Java 8)
        PARSER_REGISTRY.registerParser(String.class, ArgumentKey.of(), new StringArgumentResolver<>());
        PARSER_REGISTRY.registerParser(int.class, ArgumentKey.of(), NumberArgumentResolver.ofInteger());

        REQUIREMENT_FACTORY_REGISTRY.registerFactory(Context.class, new ContextRequirementFactory<>(CONTEXT_REGISTRY, WRAPPER_REGISTRY));
        REQUIREMENT_FACTORY_REGISTRY.registerFactory(Arg.class, new ArgumentRequirementFactory<>(WRAPPER_REGISTRY, PARSER_REGISTRY, new ArgArgumentFactory()));
    }

    @Test
    void testCreateMethodOfExecuteFactory() {
        AnnotationHolder<Arg.Mock, String, String> holder = AnnotationHolder.of(new Arg.Mock("name"), WrapFormat.notWrapped(String.class), () -> "name");
        Requirement<TestSender, String> requirement = REQUIREMENT_FACTORY_REGISTRY.create(holder);

        assertEquals("name", requirement.getName());
    }

}