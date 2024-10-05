package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import org.jetbrains.annotations.Nullable;

class EmptyParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    @Override
    public @Nullable Parser<SENDER, PARSED> getValidParser(Argument<PARSED> argument) {
        return null;
    }

}
