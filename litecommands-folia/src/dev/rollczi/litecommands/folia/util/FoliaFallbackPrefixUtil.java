package dev.rollczi.litecommands.folia.util;

import org.bukkit.plugin.Plugin;

import java.util.Locale;

public final class FoliaFallbackPrefixUtil {

    private FoliaFallbackPrefixUtil() {
    }

    public static String create(Plugin plugin) {
        return create(plugin.getName());
    }

    public static String create(String name) {
        return name.toLowerCase(Locale.ROOT)
            .replace(" ", "-");
    }

}
