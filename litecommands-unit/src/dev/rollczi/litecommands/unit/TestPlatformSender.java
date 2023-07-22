package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.platform.PlatformSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestPlatformSender implements PlatformSender {

    private final List<String> permissions = new ArrayList<>();
    private boolean permittedAll = false;

    @Override
    public String getName() {
        return "TestSender";
    }

    @Override
    public boolean hasPermission(String permission) {
        if (permittedAll) {
            return true;
        }

        return permissions.contains(permission);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PlatformSender permittedAll() {
        return builder()
            .permittedAll()
            .build();
    }

    public static class Builder {

        private final TestPlatformSender sender = new TestPlatformSender();

        public Builder permission(String permission) {
            sender.permissions.add(permission);
            return this;
        }

        public Builder permissions(String... permissions) {
            sender.permissions.addAll(Arrays.asList(permissions));
            return this;
        }

        public Builder permissions(List<String> permissions) {
            sender.permissions.addAll(permissions);
            return this;
        }

        public Builder permittedAll() {
            sender.permittedAll = true;
            return this;
        }

        public TestPlatformSender build() {
            return sender;
        }

    }

}
