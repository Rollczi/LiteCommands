package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.platform.LiteSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LitePermissions {

    private final List<String> permissions;

    public LitePermissions(Collection<String> permissions) {
        this.permissions = new ArrayList<>(permissions);
    }

    public List<String> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    public boolean isEmpty() {
        return this.permissions.isEmpty();
    }

    public LitePermissions with(LitePermissions permissions) {
        List<String> perm = new ArrayList<>(this.permissions);

        perm.addAll(permissions.permissions);

        return new LitePermissions(perm);
    }

    public static LitePermissions of(CommandMeta meta, LiteSender liteSender) {
        Set<String> perms = new HashSet<>(meta.getPermissions());
        Set<String> noPermissions = new HashSet<>();

        perms.removeAll(meta.getExcludedPermissions());

        for (String perm : perms) {
            if (liteSender.hasPermission(perm)) {
                continue;
            }

            noPermissions.add(perm);
        }

        return new LitePermissions(noPermissions);
    }

}
