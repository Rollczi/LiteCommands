package dev.rollczi.litecommands.scheme;

import java.util.List;

public interface SchemeGenerator {

    Scheme generateScheme(SchematicContext<?> result, SchemeFormat schemeFormat);

    List<String> generate(SchematicContext<?> result, SchemeFormat schemeFormat);

    static SchemeGenerator simple() {
        return new SimpleSchemeGenerator();
    }

}
