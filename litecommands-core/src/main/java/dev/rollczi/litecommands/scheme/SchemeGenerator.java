package dev.rollczi.litecommands.scheme;

import dev.rollczi.litecommands.command.FindResult;

import java.util.List;

public interface SchemeGenerator {

    Scheme generateScheme(FindResult<?> result, SchemeFormat schemeFormat);

    List<String> generate(FindResult<?> result, SchemeFormat schemeFormat);

    static SchemeGenerator simple() {
        return new SimpleSchemeGenerator();
    }

}
