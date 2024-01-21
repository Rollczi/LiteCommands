package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.minestom.test.RegisterCommand;
import dev.rollczi.litecommands.minestom.test.TestPlayer;
import net.minestom.server.entity.Player;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinestomIntegrationTest extends MineStomIntegrationSpec {

    @RegisterCommand
    final TestCommand testCommand = new TestCommand();

    @Command(name = "test")
    static class TestCommand {

        String response;

        @Execute
        void test(@Context Player sender, @Arg String test, @Arg Player player, @Arg Option<Double> optionNumber) {
            response = sender.getUsername() + " " + test + " " + player.getUsername() + " " + optionNumber.orElseGet(-1.0);
        }

    }

    @Test
    @DisplayName("Should register and execute command with player context")
    void registerAndExecuteWithPlayerContext() {
        TestPlayer rollczi = player("Rollczi");

        executeCommand(rollczi, "test siema vLucky 1.0");
        assertEquals(null, testCommand.response);
        assertThat(rollczi.getMessages()).containsExactly("Player vLucky not found! (PLAYER_NOT_FOUND)");

        player("vLucky");
        executeCommand(rollczi, "test siema vLucky 1.0");
        assertEquals("Rollczi siema vLucky 1.0", testCommand.response);
    }

}
