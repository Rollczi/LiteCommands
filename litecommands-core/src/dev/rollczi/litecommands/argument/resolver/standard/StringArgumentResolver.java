package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.RawInputParser;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

public class StringArgumentResolver<SENDER> implements RawInputParser<SENDER, String> {

    @Override
    public ParseResult<String> parse(Invocation<SENDER> invocation, Argument<String> argument, RawInput rawInput) {
        return ParseResult.success(rawInput.next());
    }

    @Override
    public Range getRange(Argument<String> argument) {
        return Range.ONE;
    }

}
