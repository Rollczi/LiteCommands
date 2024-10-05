package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import org.jetbrains.annotations.Nullable;

class MergedParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    private final Iterable<ParserSet<SENDER, PARSED>> parserSets;

    public MergedParserSetImpl(Iterable<ParserSet<SENDER, PARSED>> parserSets) {
        this.parserSets = parserSets;
    }

    @Override
    public @Nullable Parser<SENDER, PARSED> getValidParser(Argument<PARSED> argument) {
        for (ParserSet<SENDER, PARSED> parserSet : parserSets) {

            if (parserSet == null) {
                continue;
            }

            Parser<SENDER, PARSED> validParser = parserSet.getValidParser(argument);

            if (validParser != null) {
                return validParser;
            }
        }

        return null;
    }

}
