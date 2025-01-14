package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.permission.PermissionStrictHandler;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface Platform<SENDER, C extends PlatformSettings> {

    void setConfiguration(@NotNull C liteConfiguration);

    @NotNull
    C getConfiguration();

    @ApiStatus.Experimental
    PlatformSenderFactory<SENDER> getSenderFactory();

    PlatformSender createSender(SENDER nativeSender);

    default void start() {}

    default void stop() {}

    void register(CommandRoute<SENDER> commandRoute, PlatformInvocationListener<SENDER> invocationHook, PlatformSuggestionListener<SENDER> suggestionHook, PermissionStrictHandler permissionStrictHandler);

    default <LISTENER extends PlatformInvocationListener<SENDER> & PlatformSuggestionListener<SENDER>>
    void register(CommandRoute<SENDER> commandRoute, LISTENER listener, PermissionStrictHandler permissionStrictHandler) {
        register(commandRoute, listener, listener, permissionStrictHandler);
    }

    void unregister(CommandRoute<SENDER> commandRoute);

    void unregisterAll();

}
