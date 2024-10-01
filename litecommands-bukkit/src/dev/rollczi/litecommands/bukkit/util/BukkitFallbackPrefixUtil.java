package dev.rollczi.litecommands.bukkit.util;

import java.util.Locale;
import org.bukkit.plugin.Plugin;

public final class BukkitFallbackPrefixUtil {

    private BukkitFallbackPrefixUtil() {
    }

    public static String create(Plugin plugin) {
        return create(plugin.getName());
    }

    public static String create(String name) {
        return name.toLowerCase(Locale.ROOT)
            .replace(" ", "-");
    }

}
