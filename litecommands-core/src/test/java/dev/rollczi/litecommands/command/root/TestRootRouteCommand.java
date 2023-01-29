package dev.rollczi.litecommands.command.root;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.rollczi.litecommands.test.Assert.assertCollection;

class TestRootRouteCommand {

    TestPlatform platform = TestFactory.withCommands(RootCommands.class);

    @RootRoute
    static class RootCommands {
        @Execute(route = "msg")
        String msg() {
            return "msg";
        }

        @Execute(route = "reply", aliases = {"r"})
        @Permission("reply")
        String reply() {
            return "reply";
        }
    }

    @Test
    void testSimpleClassCommands() {
        this.platform.execute("msg")
            .assertSuccess()
            .assertResult("msg");

        RequiredPermissions permissions = this.platform.execute("reply")
            .assertInvalid()
            .assertResultIs(RequiredPermissions.class);

        assertCollection(List.of("reply"), permissions.getPermissions());
    }


}
