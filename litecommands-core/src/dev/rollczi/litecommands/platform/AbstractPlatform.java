package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.CommandRoute;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPlatform<SENDER, C extends PlatformSettings> implements Platform<SENDER, C> {

    protected @NotNull C settings;
    protected final PlatformSenderFactory<SENDER> senderFactory;
    protected final Map<String, CommandRoute<SENDER>> commandRoutes = new HashMap<>();

    protected AbstractPlatform(@NotNull C settings, PlatformSenderFactory<SENDER> senderFactory) {
        this.settings = settings;
        this.senderFactory = senderFactory;
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
    public PlatformSenderFactory<SENDER> getSenderFactory() {
        return senderFactory;
    }

    @Override
    public PlatformSender createSender(SENDER nativeSender) {
        return this.getSenderFactory().create(nativeSender);
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
    }

    @Override
    public final void unregister(CommandRoute<SENDER> commandRoute) {
        this.unhook(commandRoute);

        for (String name : commandRoute.names()) {
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

    protected abstract void hook(CommandRoute<SENDER> commandRoute, PlatformInvocationListener<SENDER> invocationHook, PlatformSuggestionListener<SENDER> suggestionHook);

    protected abstract void unhook(CommandRoute<SENDER> commandRoute);

}
