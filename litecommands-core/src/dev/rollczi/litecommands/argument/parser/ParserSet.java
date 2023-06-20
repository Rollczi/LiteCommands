package dev.rollczi.litecommands.argument.parser;

import java.util.Collection;
import java.util.Optional;

public interface ParserSet<SENDER, PARSED> {

    <INPUT> Optional<Parser<SENDER, INPUT, PARSED>> getParser(Class<INPUT> inType);

    Collection<Parser<SENDER, ?, PARSED>> getParsers();

}
