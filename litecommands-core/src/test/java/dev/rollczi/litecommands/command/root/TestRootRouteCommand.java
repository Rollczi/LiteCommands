package dev.rollczi.litecommands.command.root;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static dev.rollczi.litecommands.test.Assert.assertCollection;

class TestRootRouteCommand {

    TestPlatform platform = TestFactory.withCommands(RootCommands.class, RootWithPermissionCommands.class);

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

    @RootRoute
    @Permission("parent-permission")
    static class RootWithPermissionCommands {
        @Execute(route = "single-permission")
        void single() {}

        @Execute(route = "merged-permissions")
        @Permission("child-permission")
        void merged() {}
    }

    @Test
    void testSimpleClassCommands() {
        this.platform.execute("msg")
            .assertSuccess()
            .assertResult("msg");

        RequiredPermissions permissions = this.platform.execute("reply")
            .assertInvalid()
            .assertResultIs(RequiredPermissions.class);

        assertCollection(Collections.singletonList("reply"), permissions.getPermissions());
    }

    @Test
    void testCopyMetaToSubCommands() {
        RequiredPermissions single = this.platform.execute("single-permission")
            .assertInvalid()
            .assertResultIs(RequiredPermissions.class);

        assertCollection(Collections.singletonList("parent-permission"), single.getPermissions());

        RequiredPermissions merged = this.platform.execute("merged-permissions")
            .assertInvalid()
            .assertResultIs(RequiredPermissions.class);

        assertCollection(Arrays.asList("parent-permission", "child-permission"), merged.getPermissions());
    }


}
