package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.CommandRoute;
import org.jetbrains.annotations.NotNull;

public interface Platform<SENDER, C extends PlatformSettings> {

    void setConfiguration(@NotNull C liteConfiguration);

    @NotNull
    C getConfiguration();

    void register(CommandRoute<SENDER> commandRoute, PlatformInvocationHook<SENDER> invocationHook, PlatformSuggestionHook<SENDER> suggestionHook);

    void unregister(CommandRoute<SENDER> commandRoute);

    void unregisterAll();

}
