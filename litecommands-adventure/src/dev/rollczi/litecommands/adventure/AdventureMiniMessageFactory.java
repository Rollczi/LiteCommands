package dev.rollczi.litecommands.adventure;

import net.kyori.adventure.text.minimessage.MiniMessage;

final class AdventureMiniMessageFactory {

    private AdventureMiniMessageFactory() {
    }

    static MiniMessage produce(boolean supportLegacy) {
        if (!supportLegacy) {
            return MiniMessage.miniMessage();
        }

        return MiniMessage.builder()
            .preProcessor(new LegacyPreProcessor())
            .postProcessor(new LegacyPostProcessor())
            .build();
    }

}
