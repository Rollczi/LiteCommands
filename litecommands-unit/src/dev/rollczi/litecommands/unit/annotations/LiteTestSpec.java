package dev.rollczi.litecommands.unit.annotations;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.unit.TestSettings;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.unit.blocking.BlockingArgument;
import dev.rollczi.litecommands.unit.blocking.BlockingArgumentResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class LiteTestSpec {

    protected static LiteCommands<TestSender> liteCommands;
    protected static TestPlatform platform;

    @BeforeAll
    public static void beforeAll(TestInfo testInfo) {
        Optional<Class<?>> testClass = testInfo.getTestClass();

        if (!testClass.isPresent()) {
            throw new AssertionError("Test class not found");
        }

        Class<?> type = testClass.get();

        Class<?>[] commands = Arrays.stream(type.getDeclaredClasses())
            .filter(declaredClass -> declaredClass.isAnnotationPresent(Command.class) || declaredClass.isAnnotationPresent(RootCommand.class))
            .toArray(Class<?>[]::new);

        platform = new TestPlatform();

        liteCommands = configureLiteTest(LiteCommandsFactory.builder(TestSender.class, platform)
            .commands(LiteCommandsAnnotations.ofClasses(commands))
            .exceptionUnexpected((invocation, exception, chain) -> {}), type)
            .argument(BlockingArgument.class, new BlockingArgumentResolver<>())
            .build();
    }

    private static LiteCommandsBuilder<TestSender, TestSettings, ?> configureLiteTest(LiteCommandsBuilder<TestSender, TestSettings, ?> builder, Class<?> type) {
        LiteTest annotation = type.getAnnotation(LiteTest.class);

        if (annotation == null || annotation.universalHandler()) {
            builder.result(Object.class, (invocation, result, chain) -> {});
        }

        for (Field field : type.getDeclaredFields()) {
            if (field.getType() != LiteTestConfig.class) {
                continue;
            }

            try {
                field.setAccessible(true);
                Object result = field.get(null);
                LiteTestConfig configurator = (LiteTestConfig) result;

                builder = configurator.configure(builder);
            }
            catch (Exception exception) {
                throw new AssertionError("Cannot invoke method", exception);
            }
        }

        return builder;
    }

    public interface LiteTestConfig {
        LiteCommandsBuilder<TestSender, TestSettings, ?> configure(LiteCommandsBuilder<TestSender, TestSettings, ?> builder);
    }
}
