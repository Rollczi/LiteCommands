package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.ParserChainAccessor;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public abstract class ArgumentResolverChained<SENDER, PARSED> implements MultipleArgumentResolverChained<SENDER, PARSED> {

    @Override
    public ParseResult<PARSED> parse(Invocation<SENDER> invocation, Argument<PARSED> argument, RawInput input, ParserChainAccessor<SENDER> chainAccessor) {
        if (!input.hasNext()) {
            throw new IllegalArgumentException("To parse argument, you need to provide at least one argument.");
        }

        return this.parse(invocation, argument, input.next(), chainAccessor);
    }

    protected abstract ParseResult<PARSED> parse(Invocation<SENDER> invocation, Argument<PARSED> argument, String input, ParserChainAccessor<SENDER> chainAccessor);

    @Override
    public final Range getRange(Argument<PARSED> argument) {
        return Range.ONE;
    }

}
