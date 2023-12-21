package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeIndex;

import dev.rollczi.litecommands.reflect.type.TypeRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

class ParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    private final TypeRange<PARSED> parsedType;
    private final TypeIndex<Parser<SENDER, ?, PARSED>> parsers = new TypeIndex<>();

    public ParserSetImpl(TypeRange<PARSED> parsedType) {
        this.parsedType = parsedType;
    }

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

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "3.3.0")
    public Class<PARSED> getParsedType() {
        return parsedType.getBaseType();
    }

    void registerParser(Parser<SENDER, ?, PARSED> parser) {
        parsers.put(TypeRange.upwards(parser.getInputType()), parser);
    }

}
