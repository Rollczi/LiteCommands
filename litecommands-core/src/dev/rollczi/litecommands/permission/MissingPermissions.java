package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.platform.PlatformSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MissingPermissions {

    private final List<String> checkedPermissions;
    private final List<PermissionSection> missingPermissions;

    private MissingPermissions(List<String> checkedPermissions, List<PermissionSection> missingPermissions) {
        this.checkedPermissions = checkedPermissions;
        this.missingPermissions = missingPermissions;
    }

    public List<String> getChecked() {
        return Collections.unmodifiableList(checkedPermissions);
    }

    public List<PermissionSection> getPermissions() {
        return Collections.unmodifiableList(missingPermissions);
    }

    public List<String> getFlatPermissions() {
        return missingPermissions.stream().flatMap(that -> that.getPermissions().stream()).collect(Collectors.toList());
    }

    public String asJoinedText() {
        return asJoinedText(", ");
    }

    public String asJoinedText(String separator) {
        return String.join(separator, missingPermissions.stream().flatMap(that -> that.getPermissions().stream()).toArray(String[]::new));
    }

    public boolean isMissing() {
        return !missingPermissions.isEmpty();
    }

    public boolean isPermitted() {
        return missingPermissions.isEmpty();
    }

    public static MissingPermissions check(PlatformSender platformSender, MetaHolder metaHolder) {
        List<String> collected = new ArrayList<>();
        List<PermissionSection> missingPermissions = new ArrayList<>();
        MetaHolder current = metaHolder;

        while (current != null) {
            if (!current.meta().has(Meta.PERMISSIONS)) {
                current = current.parentMeta();
                continue;
            }

            List<PermissionSection> permissions = current.meta().get(Meta.PERMISSIONS);

            for (PermissionSection permission : permissions) {
                List<String> list = permission.getPermissions();
                collected.addAll(list);

                PermissionSection.Type type = permission.getType();
                if (type == PermissionSection.Type.OR) {
                    boolean matchOne = false;
                    for (String perms : list) {
                        if (platformSender.hasPermission(perms)) {
                            matchOne = true;
                        }
                    }
                    if (!matchOne) {
                        missingPermissions.add(permission);
                    }
                } else if (type == PermissionSection.Type.AND) {
                    for (String perms : list) {
                        if (!platformSender.hasPermission(perms)) {
                            missingPermissions.add(permission);
                            break;
                        }
                    }
                }
            }

            current = current.parentMeta();
        }

        return new MissingPermissions(collected, missingPermissions);
    }

    public static MissingPermissions missing(String... permissions) {
        return new MissingPermissions(Arrays.asList(permissions), Collections.singletonList(PermissionSection.and(permissions)));
    }

}
