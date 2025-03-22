package dev.rollczi.litecommands.unit.permission;

import dev.rollczi.litecommands.permission.PermissionDefaultResolver;
import dev.rollczi.litecommands.unit.TestPlatformSender;

public class TestPermissionResolver extends PermissionDefaultResolver {

    public TestPermissionResolver() {
        super((platformSender, permission) -> {
            TestPlatformSender sender = (TestPlatformSender) platformSender;
            return sender.hasPermission(permission);
        });
    }

}
