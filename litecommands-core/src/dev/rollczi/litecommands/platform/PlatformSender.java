package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.MetaKey;
import org.jetbrains.annotations.ApiStatus;

public interface PlatformSender {

    MetaKey<String> NAME = MetaKey.of("name", String.class);
    MetaKey<String> DISPLAY_NAME = MetaKey.of("display_name", String.class);
    MetaKey<Identifier> IDENTIFIER = MetaKey.of("identifier", Identifier.class);

    String getName();

    String getDisplayName();

    Identifier getIdentifier();

    /**
     * Checks if the user has a specific permission.
     *
     * @param permission the permission to check
     * @return true if the user has the permission, false otherwise
     *
     * @deprecated This method is deprecated and should not be used anymore.
     *             It will be removed in future versions of LiteCommands.
     *             Use {@link Invocation#sender()} instead and check permissions.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "3.1.1")
    boolean hasPermission(String permission);

    <T> T getProperty(MetaKey<T> key);

}
