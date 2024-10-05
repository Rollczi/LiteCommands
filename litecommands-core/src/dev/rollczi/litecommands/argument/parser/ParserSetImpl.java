package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;

import java.util.LinkedList;
import org.jetbrains.annotations.Nullable;

class ParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    private final LinkedList<Parser<SENDER, PARSED>> parsers = new LinkedList<>();

    @Override
    @Nullable
    public Parser<SENDER, PARSED> getValidParser(
        Argument<PARSED> argument
    ) {
        for (Parser<SENDER, PARSED> parser : parsers) {
            if (parser.canParse(argument)) {
                return parser;
            }
        }

        return null;
    }

    void registerParser(Parser<SENDER, PARSED> parser) {
        parsers.addFirst(parser);
    }

}
