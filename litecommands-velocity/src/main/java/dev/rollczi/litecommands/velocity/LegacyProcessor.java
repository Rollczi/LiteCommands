package dev.rollczi.litecommands.velocity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

final class LegacyProcessor implements UnaryOperator<Component> {

    private static final char AMPERSAND = '&';
    private static final char COLOR_CHAR = '\u00A7';

    private final static Replacer REPLACER = new Replacer();

    private final static Pattern LEGACY_COLOR = Pattern.compile( "(?i)" + AMPERSAND + "[0-9A-FK-ORX]" );
    private final static Replacement REPLACEMENT = new Replacement();

    @Override
    public Component apply(Component component) {
        return component.replaceText(REPLACER);
    }

    private final static class Replacer implements Consumer<TextReplacementConfig.Builder> {

        @Override
        public void accept(TextReplacementConfig.Builder builder) {
            builder
                .match(LEGACY_COLOR)
                .replacement(REPLACEMENT);
        }

    }

    private final static class Replacement implements BiFunction<MatchResult, TextComponent.Builder, ComponentLike> {

        @Override
        public ComponentLike apply(MatchResult matchResult, TextComponent.Builder builder) {
            String color = String.valueOf(COLOR_CHAR) + matchResult.group(0).charAt(1);

            return Component.text(color);
        }

    }

}
