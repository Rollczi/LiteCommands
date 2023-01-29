package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.env.FakeSender;
import org.junit.jupiter.api.Test;

class TestModernImpl {


    public static class Command {

    }

    @Test
    void test() {

        LiteCommands<FakeSender> register = LiteCommandsFactory.annotation(FakeSender.class)
            .command(new Command())
            .register();

    }

}
