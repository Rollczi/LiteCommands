package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.Nullable;

class MergedParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    private final Iterable<ParserSet<SENDER, PARSED>> parserSets;


    public MergedParserSetImpl(Iterable<ParserSet<SENDER, PARSED>> parserSets) {
        this.parserSets = parserSets;
    }

    @Override
    public @Nullable Parser<SENDER, PARSED> getValidParser(Invocation<SENDER> invocation, Argument<PARSED> argument) {
        for (ParserSet<SENDER, PARSED> parserSet : parserSets) {

            if (parserSet == null) {
                continue;
            }

            Parser<SENDER, PARSED> validParser = parserSet.getValidParser(invocation, argument);

            if (validParser != null) {
                return validParser;
            }
        }

        return null;
    }

}
