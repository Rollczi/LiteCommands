package dev.rollczi.litecommands.test;

import dev.rollczi.litecommands.platform.LiteSender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestPermissionSender implements LiteSender {

    private final TestHandle testHandle;
    private final Set<String> permissions;

    public TestPermissionSender(TestHandle testHandle, Set<String> permissions) {
        this.testHandle = testHandle;
        this.permissions = permissions;
    }

    @Override
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public Object getHandle() {
        return testHandle;
    }

    public static TestPermissionSender with(String... permissions) {
        return new TestPermissionSender(new TestHandle(), new HashSet<>(Arrays.asList(permissions)));
    }

    public static TestPermissionSender without() {
        return new TestPermissionSender(new TestHandle(), new HashSet<>());
    }

}
