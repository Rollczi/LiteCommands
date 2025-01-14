package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.PermissionSection;
import dev.rollczi.litecommands.unit.TestPlatformSender;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnyPermissionAnnotationTest extends LiteTestSpec {

    @Command(name = "test")
    @AnyPermission(value = {"test.base", "test.admin"}, type = PermissionSection.Type.OR)
    static class TestCommand {

        @Execute
        @AnyPermission(value = {"test.permission.execute", "test.admin"}, type = PermissionSection.Type.OR)
        void execute() {
        }

        @Execute(name = "cmd")
        @Permission("test.cmd")
        void cmd() {
        }
    }

    @Test
    void test() {
        MissingPermissions permissions = platform.execute("test")
            .assertFailedAs(MissingPermissions.class);

        Set<String> missing = permissions.getFlatPermissions();

        assertEquals(3, missing.size());
        assertTrue(missing.contains("test.base"));
        assertTrue(missing.contains("test.permission.execute"));

        assertThat(missing)
            .contains("test.base", "test.admin", "test.permission.execute");
    }

    @Test
    void testPermitted() {
        platform.execute(TestPlatformSender.permitted("test.admin"), "test")
            .assertSuccess();

        platform.execute(TestPlatformSender.permitted("test.base", "test.admin"), "test")
            .assertSuccess();
        platform.execute(TestPlatformSender.permitted("test.base", "test.permission.execute"), "test")
            .assertSuccess();

        platform.execute(TestPlatformSender.permitted("test.base", "test.cmd"), "test cmd")
            .assertSuccess();
        platform.execute(TestPlatformSender.permitted("test.admin", "test.cmd"), "test cmd")
            .assertSuccess();

        assertThat(platform.execute(TestPlatformSender.permitted("test.permission.execute"), "test")
            .assertFailedAs(MissingPermissions.class).getFlatPermissions()
        ).containsExactlyInAnyOrder("test.base", "test.admin");

        assertThat(platform.execute(TestPlatformSender.permitted("test.base"), "test")
            .assertFailedAs(MissingPermissions.class).getFlatPermissions()
        ).containsExactlyInAnyOrder("test.permission.execute", "test.admin");

        assertThat(platform.execute("test cmd")
            .assertFailedAs(MissingPermissions.class).getFlatPermissions()
        ).containsExactlyInAnyOrder("test.base","test.admin", "test.cmd");

        assertThat(platform.execute(TestPlatformSender.permitted("test.base"), "test cmd")
            .assertFailedAs(MissingPermissions.class).getFlatPermissions()
        ).containsExactlyInAnyOrder("test.cmd");
        assertThat(platform.execute(TestPlatformSender.permitted("test.admin"), "test cmd")
            .assertFailedAs(MissingPermissions.class).getFlatPermissions()
        ).containsExactlyInAnyOrder("test.cmd");
    }

}
