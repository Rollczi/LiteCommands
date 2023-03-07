package dev.rollczi.litecommands.modern.permission;

import dev.rollczi.litecommands.modern.platform.PlatformSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MissingPermissions {

    private final List<String> checkedPermissions;
    private final List<String> missingPermissions;

    private MissingPermissions(List<String> checkedPermissions, List<String> missingPermissions) {
        this.checkedPermissions = checkedPermissions;
        this.missingPermissions = missingPermissions;
    }

    public List<String> getChecked() {
        return Collections.unmodifiableList(checkedPermissions);
    }

    public List<String> getPermissions() {
        return Collections.unmodifiableList(missingPermissions);
    }

    public String joinPermissions() {
        return joinPermissions(", ");
    }

    public String joinPermissions(String separator) {
        return String.join(separator, missingPermissions);
    }

    public boolean isMissing() {
        return !missingPermissions.isEmpty();
    }

    public static MissingPermissions check(PlatformSender platformSender, List<String> permissions) {
        List<String> missingPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (!platformSender.hasPermission(permission)) {
                missingPermissions.add(permission);
            }
        }

        return new MissingPermissions(permissions, missingPermissions);
    }

}
