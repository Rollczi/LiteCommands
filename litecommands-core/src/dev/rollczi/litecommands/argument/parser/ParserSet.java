package dev.rollczi.litecommands.argument.parser;

import java.util.Collection;
import java.util.Optional;

public interface ParserSet<SENDER, PARSED> {

    <INPUT> Optional<Parser<SENDER, INPUT, PARSED>> getParser(Class<INPUT> inType);

    Collection<Parser<SENDER, ?, PARSED>> getParsers();

    static <SENDER, PARSED, IN> ParserSet<SENDER, PARSED> of(Class<PARSED> parsedClass, Parser<SENDER, IN, PARSED> parser) {
        ParserSetImpl<SENDER, PARSED> parserSet = new ParserSetImpl<>(parsedClass);

        parserSet.registerParser(parser);
        return parserSet;
    }

}
