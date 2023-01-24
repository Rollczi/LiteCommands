package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.core.argument.StringArgument;
import dev.rollczi.litecommands.modern.env.Sender;
import org.junit.jupiter.api.Test;

class TestModernImpl {

    public static class Command {

    }

    @Test
    void test() {

        LiteCommands<Sender> register = LiteCommandsFactory.annotation(Sender.class)
            .editor(new String())
            .command()
            .command()
            .command()
            .argument(String.class, new StringArgument<>())
            .argument(String.class, new StringArgument<>())
            .command()
            .filter()
            .register();

    }

}
