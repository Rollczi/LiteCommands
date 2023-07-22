package dev.rollczi.litecommands.annotations.argument.flag;

import dev.rollczi.litecommands.annotations.argument.AnnotationArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

class FlagArgumentResolver<SENDER> extends AnnotationArgumentResolver<SENDER, Boolean, FlagArgument> {

    public FlagArgumentResolver() {
        super(FlagArgument.class);
    }

    @Override
    public ParseResult<Boolean> parseTyped(Invocation<SENDER> invocation, FlagArgument argument, RawInput rawInput) {
        Flag flag = argument.getAnnotation();
        String key = flag.value();

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
    public Range getTypedRange(FlagArgument argument) {
        return Range.range(0, 1);
    }

    @Override
    public SuggestionResult suggestTyped(Invocation<SENDER> invocation, FlagArgument argument, SuggestionContext context) {
        return SuggestionResult.of(argument.getAnnotation().value());
    }

}
