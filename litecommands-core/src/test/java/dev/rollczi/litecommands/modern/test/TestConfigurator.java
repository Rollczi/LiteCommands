package dev.rollczi.litecommands.modern.test;

import dev.rollczi.litecommands.modern.annotation.LiteCommandsAnnotationBuilder;
import dev.rollczi.litecommands.modern.test.env.FakeSender;

public interface TestConfigurator {

    LiteCommandsAnnotationBuilder<FakeSender, ?> configure(LiteCommandsAnnotationBuilder<FakeSender, ?> builder);

}
