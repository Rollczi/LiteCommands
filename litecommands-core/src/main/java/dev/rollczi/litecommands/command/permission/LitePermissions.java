package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.LiteSender;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LitePermissions {

    private final Collection<String> permissions;

    public LitePermissions(Collection<String> permissions) {
        this.permissions = permissions;
    }

    public Collection<String> getPermissions() {
        return Collections.unmodifiableCollection(permissions);
    }

    public boolean isEmpty() {
        return this.permissions.isEmpty();
    }

    public static LitePermissions of(CommandSection section, LiteSender liteSender) {
        Set<String> perms = new HashSet<>(section.permissions());
        Set<String> noPermissions = new HashSet<>();

        perms.removeAll(section.excludePermissions());

        for (String perm : perms) {
            if (liteSender.hasPermission(perm)) {
                continue;
            }

            noPermissions.add(perm);
        }

        return new LitePermissions(noPermissions);
    }

}
