package dev.rollczi.litecommands.join;

import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.TypedArgumentResolver;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

import java.util.ArrayList;
import java.util.List;

public abstract class JoinArgumentResolver<SENDER, T> extends TypedArgumentResolver<SENDER, T, JoinArgument<T>> {

    public JoinArgumentResolver() {
        super(JoinArgument.class);
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

    @Override
    public SuggestionResult suggestTyped(Invocation<SENDER> invocation, JoinArgument<T> argument, SuggestionContext context) {
        return SuggestionResult.of("text...");
    }

}
