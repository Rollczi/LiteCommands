package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

class ChainRedirectParser<SENDER, T> implements Parser<SENDER, T> {

    private final ParserChainAccessor<SENDER> accessor;
    private final ParserChained<SENDER, T> parser;

    ChainRedirectParser(ParserChainAccessor<SENDER> accessor, ParserChained<SENDER, T> parser) {
        this.accessor = accessor;
        this.parser = parser;
    }

    @Override
    public ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
        return parser.parse(invocation, argument, input, accessor);
    }

    @Override
    public boolean canParse(Argument<T> argument) {
        return parser.canParse(argument);
    }

    @Override
    public Range getRange(Argument<T> argument) {
        return parser.getRange(argument);
    }

}
