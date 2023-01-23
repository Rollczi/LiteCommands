package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.api.StringArgument;
import dev.rollczi.litecommands.modern.env.Sender;
import dev.rollczi.litecommands.modern.extension.LiteExtension;
import org.junit.jupiter.api.Test;

class TestModernImpl {

    public static class Command {

    }

    @Test
    void test() {

        LiteCommands<Sender> liteCommands = LiteCommandsFactory.builder(Sender.class)
            .argument(String.class, new StringArgument<>())

            .withExtension(LiteExtension.annotation(), extension -> extension
                .command(Command.class)
            )

            .register();


        liteCommands.equals()


    }

}
