package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPlatform<SENDER, C extends PlatformSettings> implements Platform<SENDER, C> {

    protected @NotNull C settings;

    protected final Map<String, CommandRoute<SENDER>> commandRoutes = new HashMap<>();
    protected final Map<CommandRoute<SENDER>, PlatformInvocationListener<SENDER>> invocationHooks = new HashMap<>();
    protected final Map<CommandRoute<SENDER>, PlatformSuggestionListener<SENDER>> suggestionHooks = new HashMap<>();

    protected AbstractPlatform(@NotNull C settings) {
        this.settings = settings;
    }

    @Override
    public void setConfiguration(@NotNull C liteConfiguration) {
        this.settings = liteConfiguration;
    }

    @Override
    @NotNull
    public C getConfiguration() {
        return settings;
    }

    @Override
    public final void register(CommandRoute<SENDER> commandRoute, PlatformInvocationListener<SENDER> invocationHook, PlatformSuggestionListener<SENDER> suggestionHook) {
        for (String name : commandRoute.names()) {
            if (this.commandRoutes.containsKey(name)) {
                throw new IllegalArgumentException("Command with name " + name + " already exists");
            }
        }

        this.hook(commandRoute, invocationHook, suggestionHook);

        for (String name : commandRoute.names()) {
            this.commandRoutes.put(name, commandRoute);
        }

        this.invocationHooks.put(commandRoute, invocationHook);
        this.suggestionHooks.put(commandRoute, suggestionHook);
    }

    @Override
    public final void unregister(CommandRoute<SENDER> commandRoute) {
        this.unhook(commandRoute);

        for (String name : commandRoute.names()) {
            this.commandRoutes.remove(name);
        }

        this.invocationHooks.remove(commandRoute);
        this.suggestionHooks.remove(commandRoute);
    }

    @Override
    public final void unregisterAll() {
        for (CommandRoute<SENDER> commandRoute : this.commandRoutes.values()) {
            this.unhook(commandRoute);
        }

        this.commandRoutes.clear();
        this.invocationHooks.clear();
        this.suggestionHooks.clear();
    }

    @Override
    public CompletableFuture<CommandExecuteResult> execute(Invocation<SENDER> invocation, ParseableInput<?> arguments) {
        CommandRoute<SENDER> route = this.commandRoutes.get(invocation.name());

        if (route == null) {
            throw new IllegalArgumentException("Can not find command route with name: " + invocation.name());
        }

        PlatformInvocationListener<SENDER> listener = this.invocationHooks.get(route);

        if (listener == null) {
            throw new IllegalStateException("Can not find platform listener for command: " + invocation.name());
        }

        return listener.execute(invocation, arguments);
    }

    @Override
    public CompletableFuture<SuggestionResult> suggest(Invocation<SENDER> invocation, SuggestionInput<?> arguments) {
        CommandRoute<SENDER> route = this.commandRoutes.get(invocation.name());

        if (route == null) {
            throw new IllegalArgumentException("Can not find command route with name: " + invocation.name());
        }

        PlatformSuggestionListener<SENDER> listener = this.suggestionHooks.get(route);

        if (listener == null) {
            throw new IllegalStateException("Can not find platform listener for command: " + invocation.name());
        }

        return CompletableFuture.completedFuture(listener.suggest(invocation, arguments));
    }

    protected abstract void hook(CommandRoute<SENDER> commandRoute, PlatformInvocationListener<SENDER> invocationHook, PlatformSuggestionListener<SENDER> suggestionHook);

    protected abstract void unhook(CommandRoute<SENDER> commandRoute);

}
