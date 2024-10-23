package dev.rollczi.litecommands.annotations.argument.resolver.collector;import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OptionalCollectionTest extends LiteTestSpec {

    @Command(name = "test")
    static class OptionalCollectionTestCommand {
        @Execute
        String test(@OptionalArg List<String> names) {
            if (names == null) {
                return "null";
            }

            return String.join(", ", names);
        }
    }

    @Test
    @DisplayName("Should always return not null collection")
    void testOptionalCollectionExecute() {
        platform.execute("test")
            .assertSuccess("");

        platform.execute("test name1 name2 name3")
            .assertSuccess("name1, name2, name3");
    }

    @Test
    void testOptionalCollectionSuggest() {
        platform.suggest("test ")
            .assertSuggest("<names>");

        platform.suggest("test name1 name2 ")
            .assertSuggest("<names>");
    }

}
