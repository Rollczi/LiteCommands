package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.CommandRoute;
import org.jetbrains.annotations.NotNull;

public interface Platform<SENDER, C extends PlatformSettings> {

    void setConfiguration(@NotNull C liteConfiguration);

    @NotNull
    C getConfiguration();

    default void start() {}

    default void stop() {}

    void register(CommandRoute<SENDER> commandRoute, PlatformInvocationListener<SENDER> invocationHook, PlatformSuggestionListener<SENDER> suggestionHook);

    default <LISTENER extends PlatformInvocationListener<SENDER> & PlatformSuggestionListener<SENDER>>
    void register(CommandRoute<SENDER> commandRoute, LISTENER listener) {
        register(commandRoute, listener, listener);
    }

    void unregister(CommandRoute<SENDER> commandRoute);

    void unregisterAll();

}
