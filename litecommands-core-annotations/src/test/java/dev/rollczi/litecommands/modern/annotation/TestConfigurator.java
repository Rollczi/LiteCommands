package dev.rollczi.litecommands.modern.annotation;

import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.test.FakeSender;

public interface TestConfigurator {

    LiteCommandsBuilder<FakeSender, C, ?> configure(LiteCommandsBuilder<FakeSender, C, ?> builder);

}
