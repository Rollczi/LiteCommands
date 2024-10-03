package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.LiteCommandsBuilder;

import dev.rollczi.litecommands.unit.blocking.BlockingArgument;
import dev.rollczi.litecommands.unit.blocking.BlockingArgumentResolver;
import java.util.function.UnaryOperator;

public final class LiteCommandsTestFactory {

    private LiteCommandsTestFactory() {
    }

    public static TestPlatform startPlatform(UnaryOperator<LiteCommandsBuilder<TestSender, TestSettings, ?>> operator) {
        TestPlatform testPlatform = new TestPlatform();
        LiteCommandsBuilder<TestSender, TestSettings, ?> builder = LiteCommandsFactory.builder(TestSender.class, testPlatform);

        operator.apply(builder)
            .result(String.class, (invocation, result, chain) -> invocation.sender().sendMessage(result))
            .argument(BlockingArgument.class, new BlockingArgumentResolver<>())
            .invalidUsage((invocation, result, chain) -> {})
            .build()
            .register();

        return testPlatform;
    }

}
