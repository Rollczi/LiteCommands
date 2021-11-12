package dev.rollczi.litecommands.permssion;

import dev.rollczi.litecommands.component.EmptyTestSender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestPermissionSender extends EmptyTestSender {

    private final Set<String> permissions = new HashSet<>();

    public TestPermissionSender permission(String... permissions) {
        this.permissions.addAll(Arrays.asList(permissions));
        return this;
    }

    @Override
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

}
