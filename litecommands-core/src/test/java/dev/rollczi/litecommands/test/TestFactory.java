package dev.rollczi.litecommands.test;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.implementation.LiteFactory;
import panda.std.Result;

public final class TestFactory {

    private TestFactory() {}

    public static TestPlatform create(Configurator configurator) {
        TestPlatform testPlatform = new TestPlatform();
        LiteCommandsBuilder<TestHandle> liteCommandsBuilder = LiteFactory.builder(TestHandle.class);

        configurator.config(liteCommandsBuilder);

        liteCommandsBuilder
                .contextualBind(String.class, (testHandle, invocation) -> Result.ok("contextual"))
                .resultHandler(String.class, (v, invocation, value) -> {})
                .platform(testPlatform)
                .register();

        return testPlatform;
    }

    public static TestPlatform withCommands(Class<?>... commands) {
        return create(liteCommandsBuilder -> liteCommandsBuilder.command(commands));
    }

    public static TestPlatform withCommandsUniversalHandler(Class<?>... commands) {
        return create(liteCommandsBuilder -> liteCommandsBuilder.command(commands)
                .resultHandler(Object.class, (testHandle, invocation, value) -> {})
        );
    }

    public interface Configurator {
        void config(LiteCommandsBuilder<TestHandle> builder);
    }

}
