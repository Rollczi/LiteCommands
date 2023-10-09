package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.unit.TestSettings;
import dev.rollczi.litecommands.unit.TestSender;

public interface LiteConfig {

    LiteCommandsBuilder<TestSender, TestSettings, ?> configure(LiteCommandsBuilder<TestSender, TestSettings, ?> builder);

}
