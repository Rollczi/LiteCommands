package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.ApiStatus;
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

    @ApiStatus.Experimental
    CompletableFuture<CommandExecuteResult> execute(Invocation<SENDER> invocation, ParseableInput<?> arguments);

    @ApiStatus.Experimental
    CompletableFuture<SuggestionResult> suggest(Invocation<SENDER> invocation, SuggestionInput<?> arguments);

}
