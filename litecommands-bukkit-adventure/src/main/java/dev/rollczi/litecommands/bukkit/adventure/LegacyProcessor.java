package dev.rollczi.litecommands.bukkit.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

final class LegacyProcessor implements UnaryOperator<Component> {

    static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    @Override
    public Component apply(Component component) {
        return component.replaceText(builder -> builder.match(Pattern.compile(".*")).replacement((matchResult, build) -> LEGACY_SERIALIZER.deserialize(matchResult.group())));
    }

}
