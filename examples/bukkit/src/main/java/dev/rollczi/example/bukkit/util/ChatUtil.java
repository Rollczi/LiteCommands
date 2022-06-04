package dev.rollczi.example.bukkit.util;

import org.bukkit.ChatColor;

public final class ChatUtil {

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
