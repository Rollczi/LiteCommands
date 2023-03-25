package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.LiteTest;
import dev.rollczi.litecommands.annotations.LiteTestConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.TestConfigurator;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.route.Route;
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
