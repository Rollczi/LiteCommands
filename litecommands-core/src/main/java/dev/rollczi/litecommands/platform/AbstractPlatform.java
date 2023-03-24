package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.CommandRoute;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPlatform<SENDER, C extends LiteSettings> implements Platform<SENDER, C> {

    protected @NotNull C liteConfiguration;
    protected final Map<String, CommandRoute<SENDER>> commandRoutes = new HashMap<>();

    protected AbstractPlatform(@NotNull C liteConfiguration) {
        this.liteConfiguration = liteConfiguration;
    }

    @Override
    public void setConfiguration(@NotNull C liteConfiguration) {
        this.liteConfiguration = liteConfiguration;
    }

    @Override
    @NotNull
    public C getConfiguration() {
        return liteConfiguration;
    }

    @Override
    public final void register(CommandRoute<SENDER> commandRoute, PlatformInvocationHook<SENDER> invocationHook, PlatformSuggestionHook<SENDER> suggestionHook) {
        for (String name : commandRoute.getAllNames()) {
            if (this.commandRoutes.containsKey(name)) {
                throw new IllegalArgumentException("Command with name " + name + " already exists");
            }
        }

        this.hook(commandRoute, invocationHook, suggestionHook);

        for (String name : commandRoute.getAllNames()) {
            this.commandRoutes.put(name, commandRoute);
        }
    }

    @Override
    public final void unregister(CommandRoute<SENDER> commandRoute) {
        this.unhook(commandRoute);

        for (String name : commandRoute.getAllNames()) {
            this.commandRoutes.remove(name);
        }
    }

    @Override
    public final void unregisterAll() {
        for (CommandRoute<SENDER> commandRoute : this.commandRoutes.values()) {
            this.unhook(commandRoute);
        }

        this.commandRoutes.clear();
    }

    protected abstract void hook(CommandRoute<SENDER> commandRoute, PlatformInvocationHook<SENDER> invocationHook, PlatformSuggestionHook<SENDER> suggestionHook);

    protected abstract void unhook(CommandRoute<SENDER> commandRoute);

}
