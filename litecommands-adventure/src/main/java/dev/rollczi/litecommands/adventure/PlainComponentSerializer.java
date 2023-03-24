package dev.rollczi.litecommands.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

class PlainComponentSerializer implements ComponentSerializer<Component, Component, String> {

    private final boolean supportLegacy;

    private PlainComponentSerializer(boolean supportLegacy) {
        this.supportLegacy = supportLegacy;
    }

    @Override
    public @NotNull Component deserialize(@NotNull String input) {
        return supportLegacy ? LegacyUtil.LEGACY_SERIALIZER.deserialize(input) : Component.text(input);
    }

    @Override
    public @NotNull String serialize(@NotNull Component component) {
        throw new UnsupportedOperationException("This serializer is only for deserialization");
    }

    static PlainComponentSerializer plain(boolean supportLegacy) {
        return new PlainComponentSerializer(supportLegacy);
    }

}
