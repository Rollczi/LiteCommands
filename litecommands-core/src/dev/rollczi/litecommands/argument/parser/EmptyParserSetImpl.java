package dev.rollczi.litecommands.argument.parser;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

class EmptyParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {
    @Override
    public <INPUT> Optional<Parser<SENDER, INPUT, PARSED>> getParser(Class<INPUT> inType) {
        return Optional.empty();
    }

    @Override
    public Collection<Parser<SENDER, ?, PARSED>> getParsers() {
        return Collections.emptyList();
    }

}
