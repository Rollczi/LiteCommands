package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import dev.rollczi.litecommands.platform.PlatformSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TestPlatformSender extends AbstractPlatformSender {

    public static final MetaKey<Locale> LOCALE = MetaKey.of("locale", Locale.class);

    private final TestSender handle;

    private final List<String> permissions = new ArrayList<>();
    private boolean permittedAll = false;
    private String name = "TestSender";

    public TestPlatformSender(TestSender handle) {
        this.handle = handle;
    }

    public static PlatformSender named(String name) {
        return builder()
            .name(name)
            .build();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.of(UUID.nameUUIDFromBytes(this.getName().getBytes()));
    }

    @Override
    public Object getHandle() {
        return handle;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PlatformSender locale(Locale locale) {
        return builder()
            .locale(locale)
            .build();
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

    public boolean hasPermission(String permission) {
        if (this.permittedAll) {
            return true;
        }

        return this.permissions.contains(permission);
    }

    public static class Builder {

        private final TestPlatformSender sender = new TestPlatformSender(new TestSender());

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

        public Builder locale(Locale locale) {
            this.sender.putProperty(TestPlatformSender.LOCALE, () -> locale);
            return this;
        }

        public Builder permittedAll() {
            this.sender.permittedAll = true;
            return this;
        }

        public TestPlatformSender build() {
            return sender;
        }

        public Builder name(String name) {
            this.sender.name = name;
            return this;
        }
    }

}
