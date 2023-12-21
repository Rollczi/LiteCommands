package dev.rollczi.litecommands.argument.parser;

import java.util.Collections;
import java.util.List;

class EmptyParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {
    @Override
    public <INPUT> List<Parser<SENDER, INPUT, PARSED>> getParsers(Class<INPUT> inType) {
        return Collections.emptyList();
    }

}
