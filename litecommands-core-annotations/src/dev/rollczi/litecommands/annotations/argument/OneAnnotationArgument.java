package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.argument.parser.TypedParser;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.argument.suggestion.TypedSuggester;

public abstract class OneAnnotationArgument<SENDER, TYPE, ARG extends Argument<TYPE>> implements
    TypedParser<SENDER, RawInput, TYPE, ARG>,
    TypedSuggester<SENDER, TYPE, ARG>
{

    @Override
    public Class<RawInput> getInputType() {
        return RawInput.class;
    }

    @Override
    public Range getRange() {
        return Range.ONE;
    }

}
