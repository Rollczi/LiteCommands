package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.CommandRouteUtils;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.platform.PlatformSender;

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

    public static <SENDER> MissingPermissions check(PlatformSender platformSender, CommandRoute<SENDER> command, CommandExecutor<SENDER, ?> executor) {
        List<String> permissions = new ArrayList<>();

        CommandRouteUtils.consumeFromRootToChild(command, route -> {
            permissions.addAll(route.meta().get(Meta.PERMISSIONS));
            permissions.removeAll(route.meta().get(Meta.PERMISSIONS_EXCLUDED));
        });

        permissions.addAll(executor.meta().get(Meta.PERMISSIONS));
        permissions.removeAll(executor.meta().get(Meta.PERMISSIONS_EXCLUDED));

        return check(platformSender, permissions);
    }

    public static <SENDER> MissingPermissions check(PlatformSender platformSender, CommandRoute<SENDER> command) {
        List<String> permissions = new ArrayList<>();

        CommandRouteUtils.consumeFromRootToChild(command, route -> {
            permissions.addAll(route.meta().get(Meta.PERMISSIONS));
            permissions.removeAll(route.meta().get(Meta.PERMISSIONS_EXCLUDED));
        });

        return check(platformSender, permissions);
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
