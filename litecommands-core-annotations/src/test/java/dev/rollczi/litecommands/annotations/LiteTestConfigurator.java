package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.unit.TestSettings;
import dev.rollczi.litecommands.unit.TestSender;

public interface LiteTestConfigurator {

    LiteCommandsBuilder<TestSender, TestSettings, ?> configure(LiteCommandsBuilder<TestSender, TestSettings, ?> builder);

}
