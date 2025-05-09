package dev.rollczi.litecommands.folia;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public final class LiteCommandsFolia {

    private LiteCommandsFolia() {
    }

    /**
     * @deprecated Use {@link LiteFoliaFactory#builder(String)} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder() {
        return LiteFoliaFactory.builder();
    }

    /**
     * @deprecated Use {@link LiteFoliaFactory#builder(String)} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder(String fallbackPrefix) {
        return LiteFoliaFactory.builder(fallbackPrefix);
    }

    /**
     * @deprecated Use {@link LiteFoliaFactory#builder(String, Plugin)} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder(String fallbackPrefix, Plugin plugin) {
        return LiteFoliaFactory.builder(fallbackPrefix, plugin);
    }

    /**
     * @deprecated Use {@link LiteFoliaFactory#builder(String, Plugin, Server)} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder(String fallbackPrefix, Plugin plugin, Server server) {
        return LiteFoliaFactory.builder(fallbackPrefix, plugin, server);
    }

    /**
     * @deprecated Use {@link LiteFoliaFactory#builder(Plugin, Server, LiteFoliaSettings)} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<CommandSender, LiteFoliaSettings, B>> B builder(Plugin plugin, Server server, LiteFoliaSettings settings) {
        return LiteFoliaFactory.builder(plugin, server, settings);
    }

}
