package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.LiteConfiguration;
import dev.rollczi.litecommands.modern.command.CommandRoute;
import org.jetbrains.annotations.NotNull;

public interface Platform<SENDER, C extends LiteConfiguration> {

    void setConfiguration(@NotNull C configuration);

    @NotNull
    C getConfiguration();

    void register(CommandRoute<SENDER> commandRoute, PlatformInvocationHook<SENDER> invocationHook, PlatformSuggestionHook<SENDER> suggestionHook);

    void unregister(CommandRoute<SENDER> commandRoute);

    void unregisterAll();

}
