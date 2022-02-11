package dev.rollczi.litecommands.permssion;

import dev.rollczi.litecommands.LiteTestSender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestPermissionSender extends LiteTestSender {

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
