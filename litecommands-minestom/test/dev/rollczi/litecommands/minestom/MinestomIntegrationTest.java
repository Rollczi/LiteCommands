package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.route.Route;
import dev.rollczi.litecommands.minestom.test.RegisterCommand;
import net.minestom.server.entity.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinestomIntegrationTest extends MineStomIntegrationSpec {

    @RegisterCommand
    TestCommand testCommand = new TestCommand();

    @Route(name = "test")
    static class TestCommand {

        String response;

        @Execute
        void test(@Context Player sender, @Arg String test, @Arg Option<Double> optionNumber) {
            response = sender.getUsername() + " " + test + " " + optionNumber.orElseGet(-1.0);
        }

    }

    @Test
    @DisplayName("Should register and execute command with player context")
    void registerAndExecuteWithPlayerContext() {
        executeCommand(player("Rollczi"), "test siema 1.0");
        assertEquals("Rollczi siema 1.0", testCommand.response);
    }

}
