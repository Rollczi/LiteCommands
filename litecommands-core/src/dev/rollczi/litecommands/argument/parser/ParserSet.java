package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import org.jetbrains.annotations.Nullable;

public interface ParserSet<SENDER, PARSED> {

    default Parser<SENDER, PARSED> getValidParserOrThrow(Argument<PARSED> argument) {
        Parser<SENDER, PARSED> parser = this.getValidParser(argument);

        if (parser == null) {
            String simpleName = argument.getType().getRawType().getSimpleName();
            throw new IllegalArgumentException("No parser found for argument " + simpleName + " with key " + argument.getKey());
        }

        return parser;
    }

    @Nullable
    Parser<SENDER, PARSED>
    getValidParser(Argument<PARSED> argument);

    static <SENDER, PARSED> ParserSet<SENDER, PARSED> of(Parser<SENDER, PARSED> parser) {
        ParserSetImpl<SENDER, PARSED> parserSet = new ParserSetImpl<>();

        parserSet.registerParser(parser);
        return parserSet;
    }

}
