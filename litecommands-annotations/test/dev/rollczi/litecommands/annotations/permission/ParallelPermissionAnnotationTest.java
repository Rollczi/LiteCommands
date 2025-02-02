package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.unit.TestPlatformSender;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ParallelPermissionAnnotationTest extends LiteTestSpec {

    @Command(name = "test")
    @Permission("test.base")
    @Permission("test.admin")
    static class TestCommand {

        @Execute
        @Permission("test.permission.execute")
        @Permission("test.admin")
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

        List<String> missing = permissions.getPermissions();

        assertThat(missing)
            .containsExactly("test.permission.execute",  "test.admin", "test.base");
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
            .assertFailedAs(MissingPermissions.class).getPermissions()
        ).containsExactlyInAnyOrder("test.base", "test.admin");

        assertThat(platform.execute(TestPlatformSender.permitted("test.base"), "test")
            .assertFailedAs(MissingPermissions.class).getPermissions()
        ).containsExactlyInAnyOrder("test.permission.execute", "test.admin");

        assertThat(platform.execute("test cmd")
            .assertFailedAs(MissingPermissions.class).getPermissions()
        ).containsExactlyInAnyOrder("test.base", "test.admin", "test.cmd");

        assertThat(platform.execute(TestPlatformSender.permitted("test.base"), "test cmd")
            .assertFailedAs(MissingPermissions.class).getPermissions()
        ).containsExactlyInAnyOrder("test.cmd");
        assertThat(platform.execute(TestPlatformSender.permitted("test.admin"), "test cmd")
            .assertFailedAs(MissingPermissions.class).getPermissions()
        ).containsExactlyInAnyOrder("test.cmd");
    }

}
