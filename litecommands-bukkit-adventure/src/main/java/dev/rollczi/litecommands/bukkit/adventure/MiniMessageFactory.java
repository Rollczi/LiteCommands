package dev.rollczi.litecommands.bukkit.adventure;

import net.kyori.adventure.text.minimessage.MiniMessage;

final class MiniMessageFactory {

    private MiniMessageFactory() {
    }

    static MiniMessage produce() {
        return MiniMessage.builder()
                .postProcessor(new LegacyProcessor())
                .build();
    }

}
