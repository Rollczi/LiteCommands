package dev.rollczi.litecommands.join;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.profile.ProfiledParser;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

import java.util.ArrayList;
import java.util.List;

public abstract class JoinArgumentResolver<SENDER, T> extends ProfiledParser<SENDER, T, JoinProfile> {

    protected JoinArgumentResolver() {
        super(JoinProfile.KEY);
    }

    @Override
    public ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput rawInput, JoinProfile joinProfile) {
        int limit = joinProfile.getLimit();
        List<String> values = new ArrayList<>();

        if (!rawInput.hasNext()) {
            return ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
        }

        while (limit > 0 && rawInput.hasNext()) {
            values.add(rawInput.next());
            limit--;
        }

        return ParseResult.success(this.join(joinProfile, values));
    }

    protected abstract T join(JoinProfile argument, List<String> values);

    @Override
    public Range getRange(Argument<T> argument, JoinProfile joinContainer) {
        return Range.range(1, joinContainer.getLimit());
    }

}
