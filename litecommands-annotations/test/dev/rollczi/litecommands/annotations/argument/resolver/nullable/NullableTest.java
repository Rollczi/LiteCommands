package dev.rollczi.litecommands.annotations.argument.resolver.nullable;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

class NullableTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder
        .advanced();

    @Command(name = "nullable")
    static class NullableTestCommand {
        @Execute
        String executeNullable(@Arg(value = "test", nullable = true) String name) {
            return name;
        }

        @Execute(name = "with args")
        String execute(@Arg String name) {
            return name;
        }
    }

    @Test
    void testNullableExecute() {
        platform.execute("nullable name")
            .assertSuccess("name");

        platform.execute("nullable")
            .assertSuccess();
    }

    @Test
    void testNullableSuggest() {
        platform.suggest("nullable ")
            .assertSuggest("<test>", "with");
    }

}
