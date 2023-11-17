package dev.rollczi.litecommands.jda.permission;

import net.dv8tion.jda.api.Permission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DiscordMissingPermissions {

    private final List<Permission> checkedPermissions;
    private final List<Permission> missingPermissions;

    public DiscordMissingPermissions(List<Permission> checkedPermissions, List<Permission> missingPermissions) {
        this.checkedPermissions = checkedPermissions;
        this.missingPermissions = missingPermissions;
    }

    public List<Permission> getChecked() {
        return Collections.unmodifiableList(checkedPermissions);
    }

    public List<Permission> getPermissions() {
        return Collections.unmodifiableList(missingPermissions);
    }

    public String asJoinedText() {
        return asJoinedText(", ");
    }

    public String asJoinedText(String separator) {
        return missingPermissions.stream()
            .map(Permission::getName)
            .collect(Collectors.joining(separator));
    }

    public boolean isMissing() {
        return !missingPermissions.isEmpty();
    }

    public boolean isPermitted() {
        return missingPermissions.isEmpty();
    }

}
