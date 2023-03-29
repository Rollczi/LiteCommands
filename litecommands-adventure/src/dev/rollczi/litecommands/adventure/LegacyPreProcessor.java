package dev.rollczi.litecommands.adventure;

import java.util.function.UnaryOperator;

final class LegacyPreProcessor implements UnaryOperator<String> {

    @Override
    public String apply(String component) {
        return component.replace("§", "&");
    }

}
