package dev.rollczi.litecommands.bukkit.adventure;

import net.kyori.adventure.text.minimessage.MiniMessage;

public final class MiniMessageFactory {

    private MiniMessageFactory() {
    }

    public static MiniMessage produce() {
        return MiniMessage.builder()
                .postProcessor(new LegacyProcessor())
                .build();
    }

}
