package dev.rollczi.litecommands.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

final class LegacyPostProcessor implements UnaryOperator<Component> {

    private static final TextReplacementConfig LEGACY_REPLACEMENT_CONFIG = TextReplacementConfig.builder()
            .match(Pattern.compile(".*"))
            .replacement((matchResult, build) -> LegacyUtil.LEGACY_SERIALIZER.deserialize(matchResult.group()))
            .build();

    @Override
    public Component apply(Component component) {
        return component.replaceText(LEGACY_REPLACEMENT_CONFIG);
    }

}
