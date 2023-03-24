package dev.rollczi.litecommands.annotation;

import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.test.FakeConfig;
import dev.rollczi.litecommands.test.FakeSender;

public interface TestConfigurator {

    LiteCommandsBuilder<FakeSender, FakeConfig, ?> configure(LiteCommandsBuilder<FakeSender, FakeConfig, ?> builder);

}
