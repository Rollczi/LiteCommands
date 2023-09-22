package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.execute.Execute;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.Permission;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PermissionAnnotationTest extends LiteTestSpec {

    @dev.rollczi.litecommands.command.Command(name = "test")
    @Permission("test.permission")
    static class Command {

        @Execute
        @Permission("test.permission.execute")
        void execute() {}

    }

    @Test
    void test() {
        MissingPermissions permissions = platform.execute("test")
            .assertFailedAs(MissingPermissions.class);

        List<String> missing = permissions.getPermissions();

        assertEquals(2, missing.size());
        assertTrue(missing.contains("test.permission"));
        assertTrue(missing.contains("test.permission.execute"));
    }

}
