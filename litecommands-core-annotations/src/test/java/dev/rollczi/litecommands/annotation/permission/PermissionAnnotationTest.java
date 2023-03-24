package dev.rollczi.litecommands.annotation.permission;

import dev.rollczi.litecommands.annotation.LiteTest;
import dev.rollczi.litecommands.annotation.LiteTestConfig;
import dev.rollczi.litecommands.annotation.LiteTestSpec;
import dev.rollczi.litecommands.annotation.TestConfigurator;
import dev.rollczi.litecommands.annotation.execute.Execute;
import dev.rollczi.litecommands.annotation.route.Route;
import dev.rollczi.litecommands.permission.MissingPermissions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@LiteTest
class PermissionAnnotationTest extends LiteTestSpec {

    @LiteTestConfig
    static TestConfigurator config() {
        return builder ->
            builder.editor("", context -> context);
    }

    @Route(name = "test")
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
