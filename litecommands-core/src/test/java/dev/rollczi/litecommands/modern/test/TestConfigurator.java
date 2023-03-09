package dev.rollczi.litecommands.modern.test;

import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.test.env.FakeSender;

public interface TestConfigurator {

    LiteCommandsBuilder<FakeSender, ?> configure(LiteCommandsBuilder<FakeSender, ?> builder);

}
