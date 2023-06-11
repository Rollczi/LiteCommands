package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.input.RawInput;
import dev.rollczi.litecommands.argument.parser.ArgumentTypedParser;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.TypedSuggester;

public abstract class OneAnnotationArgument<SENDER, TYPE, ARG extends Argument<TYPE>> implements
    ArgumentTypedParser<SENDER, RawInput, TYPE, ARG>,
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
