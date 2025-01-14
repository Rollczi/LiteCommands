package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChildCommandPermissionHandleTest extends LiteTestSpec {

    @Command(name = "main")
    static class MainCommand {
        @Execute
        void execute() {
        }

        @Execute(name = "info")
        @Permission("main.info")
        void info() {
        }

        @Execute(name = "test")
        @Permission("main.test")
        void test(@Arg String arg) {
        }

        @Execute(name = "test")
        @Permission("main.test.2")
        void test(@Arg String arg, @Arg String arg2) {
        }

    }

    @Test
    void testSinglePermission() {
        MissingPermissions permissions = platform.execute("main info")
            .assertFailedAs(MissingPermissions.class);

        assertEquals(1, permissions.getPermissions().size());
        assertEquals(Collections.singletonList("main.info"), permissions.getPermissions().get(0).getPermissions());
    }

    @Test
    void testPermissionInSameRoute() {
        MissingPermissions permissions = platform.execute("main test value")
            .assertFailedAs(MissingPermissions.class);

        assertEquals(1, permissions.getPermissions().size());
        assertThat(permissions.getFlatPermissions())
            .containsOnlyOnce("main.test");

        permissions = platform.execute("main test value value2")
            .assertFailedAs(MissingPermissions.class);

        assertEquals(1, permissions.getPermissions().size());
        assertThat(permissions.getFlatPermissions())
            .containsOnlyOnce("main.test.2");
    }

}
