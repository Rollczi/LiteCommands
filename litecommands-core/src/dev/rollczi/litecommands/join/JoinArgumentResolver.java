package dev.rollczi.litecommands.join;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.TypedParser;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

import java.util.ArrayList;
import java.util.List;

public abstract class JoinArgumentResolver<SENDER, T> implements TypedParser<SENDER, T, JoinArgument<T>> {

    @Override
    public Class<? extends Argument> getArgumentType() {
        return JoinArgument.class;
    }

    @Override
    public ParseResult<T> parseTyped(Invocation<SENDER> invocation, JoinArgument<T> argument, RawInput rawInput) {
        int limit = argument.getLimit();
        List<String> values = new ArrayList<>();

        if (!rawInput.hasNext()) {
            return ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
        }

        while (limit > 0 && rawInput.hasNext()) {
            values.add(rawInput.next());
            limit--;
        }

        return ParseResult.success(this.join(argument, values));
    }

    protected abstract T join(JoinArgument<T> argument, List<String> values);

    @Override
    public Range getTypedRange(JoinArgument<T> argument) {
        return Range.range(1, argument.getLimit());
    }

}
