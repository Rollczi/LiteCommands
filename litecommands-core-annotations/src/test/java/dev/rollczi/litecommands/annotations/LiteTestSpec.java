package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.annotations.route.RootRoute;
import dev.rollczi.litecommands.annotations.route.Route;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestSettings;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            .filter(declaredClass -> declaredClass.isAnnotationPresent(Route.class) || declaredClass.isAnnotationPresent(RootRoute.class))
            .toArray(Class<?>[]::new);

        platform = LiteCommandsTestFactory.startPlatform(builder -> {
            builder.extension(LiteAnnotationsExtension.create(), extension -> extension
                .command(commands)
            );

            return configureLiteTest(builder, type);
        });
    }

    private static LiteCommandsBuilder<TestSender, TestSettings, ?> configureLiteTest(LiteCommandsBuilder<TestSender, TestSettings, ?> builder, Class<?> type) {
        LiteTest annotation = type.getAnnotation(LiteTest.class);

        if (annotation == null) {
            throw new AssertionError("Test class must be annotated with @LiteTest");
        }

        if (annotation.universalHandler()) {
            builder.resultHandler(Object.class, (invocation, result) -> {});
        }

        List<Method> methods = Arrays.stream(type.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(LiteTestConfig.class))
            .collect(Collectors.toList());

        for (Method method : methods) {
            try {
                method.setAccessible(true);
                Object result = method.invoke(null);

                if (!(result instanceof LiteTestConfigurator)) {
                    throw new AssertionError("@LiteTestConfig must return TestConfigurator");
                }

                LiteTestConfigurator configurator = (LiteTestConfigurator) result;

                builder = configurator.configure(builder);

            }
            catch (Exception e) {
                throw new AssertionError("Cannot invoke method", e);
            }
        }

        return builder;
    }

}
