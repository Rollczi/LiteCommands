package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.CommandRouteUtils;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.platform.PlatformSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public String asJoinedText() {
        return asJoinedText(", ");
    }

    public String asJoinedText(String separator) {
        return String.join(separator, missingPermissions);
    }

    public boolean isMissing() {
        return !missingPermissions.isEmpty();
    }

    public boolean isPermitted() {
        return missingPermissions.isEmpty();
    }

    public static MissingPermissions check(PlatformSender platformSender, MetaHolder metaHolder) {
        List<String> collected = new ArrayList<>();
        List<String> missingPermissions = new ArrayList<>();
        MetaHolder current = metaHolder;

        while (current != null) {
            if (!current.meta().has(Meta.PERMISSIONS)) {
                current = current.parentMeta();
                continue;
            }

            List<String> permissions = current.meta().get(Meta.PERMISSIONS);

            for (String permission : permissions) {
                collected.add(permission);

                if (!platformSender.hasPermission(permission)) {
                    missingPermissions.add(permission);
                }
            }

            current = current.parentMeta();
        }

        return new MissingPermissions(collected, missingPermissions);
    }

}
