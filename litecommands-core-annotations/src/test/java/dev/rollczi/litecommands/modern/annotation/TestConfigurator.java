package dev.rollczi.litecommands.modern.annotation;

import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.test.FakeConfig;
import dev.rollczi.litecommands.modern.test.FakeSender;

public interface TestConfigurator {

    LiteCommandsBuilder<FakeSender, FakeConfig, ?> configure(LiteCommandsBuilder<FakeSender, FakeConfig, ?> builder);

}
