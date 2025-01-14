package dev.rollczi.litecommands.permission;

import java.util.Arrays;
import java.util.List;

public class PermissionSection {
    private final List<String> permissions;
    private final Type type;

    public PermissionSection(List<String> permissions, Type type) {
        this.permissions = permissions;
        this.type = type;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public Type getType() {
        return type;
    }

    public static PermissionSection of(List<String> permissions, Type type) {
        return new PermissionSection(permissions, type);
    }

    public static PermissionSection and(List<String> permissions) {
        return new PermissionSection(permissions, Type.AND);
    }

    public static PermissionSection and(String... permissions) {
        return new PermissionSection(Arrays.asList(permissions), Type.AND);
    }

    public static PermissionSection or(List<String> permissions) {
        return new PermissionSection(permissions, Type.OR);
    }

    public enum Type {
        OR,
        AND
    }
}
