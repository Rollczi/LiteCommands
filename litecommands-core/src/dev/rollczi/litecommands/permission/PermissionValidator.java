package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.platform.PlatformSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface PermissionValidator {
    void check(PlatformSender platformSender, Set<Set<String>> permissions, List<String> collected, List<String> missingPermissions);

    PermissionValidator STRICT = (platformSender, permissions, collected, missingPermissions) -> {
        for (Set<String> permissionSet : permissions) {
            collected.addAll(permissionSet);

            for (String perms : permissionSet) {
                if (!platformSender.hasPermission(perms)) {
                    missingPermissions.add(perms);
                }
            }
        }
    };

    PermissionValidator PARALLEL = (platformSender, permissions, collected, missingPermissions) -> {
        for (Set<String> permissionSet : permissions) {
            collected.addAll(permissionSet);
        }
        List<String> missing = new ArrayList<>();
        boolean match = false;
        for (Set<String> permissionSet : permissions) {
            boolean containMissing = false;
            for (String perms : permissionSet) {
                if (!platformSender.hasPermission(perms)) {
                    missing.add(perms);
                    containMissing = true;
                    break;
                }
            }
            if (!containMissing) {
                match = true;
            }
        }
        if (!match) {
            missingPermissions.addAll(missing);
        }
    };
}
