package dev.rollczi.litecommands.annotations.literal;import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

class LiteralTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder
        .advanced();

    @Command(name = "user")
    static class LiteralTestCommand {
        @Execute
        String setGroup(@Arg String name, @Literal("group set") String literal, @Arg String group) {
            return name + " " + literal + " " + group;
        }

        @Execute
        String ban(@Arg String name, @Literal("ban") String ban, @Join String reason) {
            return name + " " + ban + " " + reason;
        }
        @Execute
        String getName(@Arg String name, @Literal("get name") String literal) {
            return name + " " + literal;
        }

        @Execute
        String getUuid(@Arg String name, @Literal("get uuid") String literal) {
            return name + " " + literal;
        }

    }

    @Test
    void testLiteralExecute() {
        platform.execute("user Rollczi group set Admin")
            .assertSuccess("Rollczi group set Admin");

        platform.execute("user Rollczi get name")
            .assertSuccess("Rollczi get name");

        platform.execute("user Rollczi get uuid")
            .assertSuccess("Rollczi get uuid");

        platform.execute("user Rollczi ban test reason")
            .assertSuccess("Rollczi ban test reason");
    }

    @Test
    void testLiteralSuggest() {
        platform.suggest("user Rollczi ")
            .assertSuggest("group set", "get name", "get uuid", "ban");

        platform.suggest("user Rollczi group set ")
            .assertSuggest("<group>");

        platform.suggest("user Rollczi ban ")
            .assertSuggest("<reason>");

        platform.suggest("user Rollczi get name ")
            .assertSuggest();

        platform.suggest("user Rollczi get uuid ")
            .assertSuggest();

        platform.suggest("user Rollczi get ")
            .assertSuggest("name", "uuid");
    }

}
