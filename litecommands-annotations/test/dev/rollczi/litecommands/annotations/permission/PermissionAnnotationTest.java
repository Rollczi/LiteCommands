package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PermissionAnnotationTest extends LiteTestSpec {

    @Command(name = "test")
    @Permission("test.permission")
    static class TestCommand {

        @Execute
        @Permission("test.permission.execute")
        void execute() {}

    }

    @Test
    void test() {
        MissingPermissions permissions = platform.execute("test")
            .assertFailedAs(MissingPermissions.class);

        Set<String> missing = permissions.getFlatPermissions();

        assertEquals(2, missing.size());
        assertTrue(missing.contains("test.permission"));
        assertTrue(missing.contains("test.permission.execute"));
    }

}
