package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.meta.MetaKey;

public interface PlatformSender {

    MetaKey<String> NAME = MetaKey.of("name", String.class);
    MetaKey<String> DISPLAY_NAME = MetaKey.of("display_name", String.class);
    MetaKey<Identifier> IDENTIFIER = MetaKey.of("identifier", Identifier.class);

    String getName();

    String getDisplayName();

    Identifier getIdentifier();

    boolean hasPermission(String permission);

    <T> T getProperty(MetaKey<T> key);

}
