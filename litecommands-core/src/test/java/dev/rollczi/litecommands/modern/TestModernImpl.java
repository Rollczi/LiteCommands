package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.api.StringArgument;
import dev.rollczi.litecommands.modern.env.Sender;
import dev.rollczi.litecommands.modern.extension.annotation.LiteCommandsAnnotationExtension;
import org.junit.jupiter.api.Test;

class TestModernImpl {

    @Test
    void test() {
        LiteCommands<Sender> liteCommands = new LiteCommandsBaseBuilder<>(Sender.class)
            .argument(String.class, new StringArgument<>())
            .withExtension(new LiteCommandsAnnotationExtension<>())
            .register();


    }

}
