package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.Nullable;

class EmptyParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    @Override
    public @Nullable Parser<SENDER, PARSED> getValidParser(Invocation<SENDER> invocation, Argument<PARSED> argument) {
        return null;
    }

}
