package dev.rollczi.litecommands.modern.annotation;

import dev.rollczi.litecommands.modern.LiteCommands;
import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsFactory;
import dev.rollczi.litecommands.modern.annotation.route.RootRoute;
import dev.rollczi.litecommands.modern.annotation.route.Route;
import dev.rollczi.litecommands.modern.test.FakeConfig;
import dev.rollczi.litecommands.modern.test.FakePlatform;
import dev.rollczi.litecommands.modern.test.FakeSender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LiteTestSpec {

    protected static LiteCommands<FakeSender> liteCommands;
    protected static FakePlatform platform;

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

        platform = new FakePlatform();
        LiteCommandsBuilder<FakeSender, FakeConfig, ?> builder = LiteCommandsFactory.builder(FakeSender.class, new FakeConfig())
            .platform(platform)
            .extension(LiteAnnotationExtension.create(), extension -> extension
                .command(commands)
            );

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

                if (!(result instanceof TestConfigurator)) {
                    throw new AssertionError("@LiteTestConfig must return TestConfigurator");
                }

                TestConfigurator configurator = (TestConfigurator) result;

                builder = configurator.configure(builder);

            } catch (Exception e) {
                throw new AssertionError("Cannot invoke method", e);
            }
        }

        liteCommands = builder.register();
    }

}
