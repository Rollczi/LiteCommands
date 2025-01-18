package dev.rollczi.litecommands.permission;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class PermissionSet {

    private final Set<String> permissions;

    public PermissionSet(Collection<String> permissions) {
        this.permissions = Collections.unmodifiableSet(new LinkedHashSet<>(permissions));
    }

    public PermissionSet(String... permissions) {
        this.permissions = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(permissions)));
    }

    public Set<String> getPermissions() {
        return permissions;
    }

}
