package dev.rollczi.litecommands.annotations.reflect;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

class SuperMethodTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand extends BaseCommand {
        @Execute(name = "new")
        String executeNew() {
            return "new";
        }
    }

    static class BaseCommand {
        @Execute(name = "legacy")
        String executeLegacy() {
            return "legacy";
        }
    }

    @Test
    void testSuperMethod() {
        platform.execute("test legacy")
            .assertSuccess("legacy");

        platform.execute("test new")
            .assertSuccess("new");
    }

}
