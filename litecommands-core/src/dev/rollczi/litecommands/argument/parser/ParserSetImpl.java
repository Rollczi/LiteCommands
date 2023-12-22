package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeIndex;

import dev.rollczi.litecommands.reflect.type.TypeRange;
import org.jetbrains.annotations.Nullable;

class ParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    private final TypeIndex<Parser<SENDER, ?, PARSED>> parsers = new TypeIndex<>();

    @Override
    @SuppressWarnings("unchecked")
    @Nullable
    public <INPUT> Parser<SENDER, INPUT, PARSED> getValidParser(
        Class<INPUT> input,
        Invocation<SENDER> invocation,
        Argument<PARSED> argument
    ) {
        for (Parser<SENDER, ?, PARSED> parser : this.parsers.get(input)) {
            Parser<SENDER, INPUT, PARSED> castedParser = (Parser<SENDER, INPUT, PARSED>) parser;

            if (castedParser.canParse(invocation, argument, input)) {
                return castedParser;
            }
        }

        return null;
    }

    void registerParser(Parser<SENDER, ?, PARSED> parser) {
        parsers.put(TypeRange.upwards(parser.getInputType()), parser);
    }

}
