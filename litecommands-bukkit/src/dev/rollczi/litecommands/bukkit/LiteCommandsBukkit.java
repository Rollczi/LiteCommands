package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public final class LiteCommandsBukkit {

    private LiteCommandsBukkit() {
    }

    /**
     * @deprecated Use {@link LiteBukkitFactory#builder(String)} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder() {
        return LiteBukkitFactory.builder();
    }

    /**
     * @deprecated Use {@link LiteBukkitFactory#builder(String)} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder(String fallbackPrefix) {
        return LiteBukkitFactory.builder(fallbackPrefix);
    }

    /**
     * @deprecated Use {@link LiteBukkitFactory#builder(String, Plugin)} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder(String fallbackPrefix, Plugin plugin) {
        return LiteBukkitFactory.builder(fallbackPrefix, plugin);
    }

    /**
     * @deprecated Use {@link LiteBukkitFactory#builder(String, Plugin, Server)} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder(String fallbackPrefix, Plugin plugin, Server server) {
        return LiteBukkitFactory.builder(fallbackPrefix, plugin, server);
    }

    /**
     * @deprecated Use {@link LiteBukkitFactory#builder(Plugin, Server, LiteBukkitSettings)} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<CommandSender, LiteBukkitSettings, B>> B builder(Plugin plugin, Server server, LiteBukkitSettings settings) {
        return LiteBukkitFactory.builder(plugin, server, settings);
    }

}
