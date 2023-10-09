package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import dev.rollczi.litecommands.platform.PlatformSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TestPlatformSender extends AbstractPlatformSender {

    private final List<String> permissions = new ArrayList<>();
    private boolean permittedAll = false;

    @Override
    public String getName() {
        return "TestSender";
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.of(UUID.nameUUIDFromBytes(this.getName().getBytes()));
    }

    @Override
    public boolean hasPermission(String permission) {
        if (this.permittedAll) {
            return true;
        }

        return this.permissions.contains(permission);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PlatformSender permittedAll() {
        return builder()
            .permittedAll()
            .build();
    }

    public static PlatformSender permitted(String... permissions) {
        return builder()
            .permissions(permissions)
            .build();
    }

    public static class Builder {

        private final TestPlatformSender sender = new TestPlatformSender();

        public Builder permission(String permission) {
            this.sender.permissions.add(permission);
            return this;
        }

        public Builder permissions(String... permissions) {
            this.sender.permissions.addAll(Arrays.asList(permissions));
            return this;
        }

        public Builder permissions(List<String> permissions) {
            this.sender.permissions.addAll(permissions);
            return this;
        }

        public Builder permittedAll() {
            this.sender.permittedAll = true;
            return this;
        }

        public TestPlatformSender build() {
            return sender;
        }

    }

}
