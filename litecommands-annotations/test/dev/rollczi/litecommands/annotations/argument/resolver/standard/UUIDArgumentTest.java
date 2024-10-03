package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UUIDArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        UUID test(@Arg UUID uuid) {
            return uuid;
        }
    }

    @Test
    void test() {
        UUID uuid = UUID.fromString("e44d9727-9da7-4f68-b608-7018a085fc6b");

        // with dashes
        platform.execute("test " + uuid)
            .assertSuccess(uuid);

        // without dashes
        platform.execute("test " + uuid.toString().replace("-", ""))
            .assertSuccess(uuid);
    }

    @Test
    void testInvalid() {
        platform.execute("test invalid")
            .assertFailure();
    }

    @Test
    void testSuggestions() {
        platform.suggest("test ")
            .assertNotEmpty()
            .assertCorrect(suggestion -> platform.execute("test " + suggestion.multilevel()).assertSuccess());
    }
}
