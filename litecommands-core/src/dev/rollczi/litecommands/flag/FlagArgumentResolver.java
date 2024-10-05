package dev.rollczi.litecommands.flag;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.profile.ProfiledMultipleArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

public class FlagArgumentResolver<SENDER> extends ProfiledMultipleArgumentResolver<SENDER, Boolean, FlagProfile> {

    public FlagArgumentResolver() {
        super(FlagProfile.NAMESPACE);
    }

    @Override
    public ParseResult<Boolean> parse(Invocation<SENDER> invocation, Argument<Boolean> argument, RawInput rawInput, FlagProfile meta) {
        String key = argument.getName();

        if (!rawInput.hasNext()) {
            return ParseResult.success(false);
        }

        if (rawInput.seeNext().equals(key)) {
            rawInput.next();
            return ParseResult.success(true);
        }

        return ParseResult.success(false);
    }

    @Override
    public Range getRange(Argument<Boolean>  argument, FlagProfile meta) {
        return Range.range(0, 1);
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Boolean>  argument, SuggestionContext context, FlagProfile meta) {
        return SuggestionResult.of(argument.getName());
    }

}
