package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.Platform;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Test;

public class JDAInputTest extends JDATestSpec {

    @Command(name = "test")
    static class TestCommand {
        @Execute
        String test(@Arg String text) {
            return text;
        }
    }

    @Test
    void testStringArgument() {
        LiteCommands<User> liteCommands = LiteJDAFactory.builder(jda)
            .commands(new TestCommand())
            .build();

        Platform<User, ?> platform = liteCommands.getInternal().getPlatform();

        CommandExecuteResult result = platform.execute(new Invocation<>(user, new JDAPlatformSender(user, null), "test", "test", raw), raw)
            .join();

        assertThat(result.getResult().toString())
            .isEqualTo("1 2 3");
    }

}
