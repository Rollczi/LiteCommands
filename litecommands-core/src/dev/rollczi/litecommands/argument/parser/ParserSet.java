package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.Nullable;

public interface ParserSet<SENDER, PARSED> {

    default Parser<SENDER, PARSED> getValidParserOrThrow(
        Invocation<SENDER> invocation,
        Argument<PARSED> argument
    ) {
        Parser<SENDER, PARSED> parser = this.getValidParser(invocation, argument);

        if (parser == null) {
            String simpleName = argument.getWrapperFormat().parsedType().getRawType().getSimpleName();
            throw new IllegalArgumentException("No parser found for argument " + simpleName);
        }

        return parser;
    }

    @Nullable
    Parser<SENDER, PARSED>
    getValidParser(Invocation<SENDER> invocation, Argument<PARSED> argument);

    static <SENDER, PARSED> ParserSet<SENDER, PARSED> of(Parser<SENDER, PARSED> parser) {
        ParserSetImpl<SENDER, PARSED> parserSet = new ParserSetImpl<>();

        parserSet.registerParser(parser);
        return parserSet;
    }

}
