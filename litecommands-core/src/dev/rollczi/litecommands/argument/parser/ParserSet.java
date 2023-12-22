package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface ParserSet<SENDER, PARSED> {

    default <INPUT> Parser<SENDER, INPUT, PARSED> getValidParserOrThrow(
        Class<INPUT> input,
        Invocation<SENDER> invocation,
        Argument<PARSED> argument
    ) {
        Parser<SENDER, INPUT, PARSED> parser = this.getValidParser(input, invocation, argument);

        if (parser == null) {
            String simpleName = argument.getWrapperFormat().parsedType().getRawType().getSimpleName();
            throw new IllegalArgumentException("No parser found for argument " + simpleName);
        }

        return parser;
    }

    @Nullable
    <INPUT> Parser<SENDER, INPUT, PARSED>
    getValidParser(Class<INPUT> input, Invocation<SENDER> invocation, Argument<PARSED> argument);

    static <SENDER, PARSED, IN> ParserSet<SENDER, PARSED> of(Parser<SENDER, IN, PARSED> parser) {
        ParserSetImpl<SENDER, PARSED> parserSet = new ParserSetImpl<>();

        parserSet.registerParser(parser);
        return parserSet;
    }

}
