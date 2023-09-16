package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.argument.parser.TypedParser;
import dev.rollczi.litecommands.argument.suggester.TypedSuggester;

@SuppressWarnings("rawtypes")
public abstract class AnnotationArgumentResolver<SENDER, TYPE, ARG extends Argument<TYPE>> implements
    TypedParser<SENDER, RawInput, TYPE, ARG>,
    TypedSuggester<SENDER, TYPE, ARG>
{

    private final Class<? extends Argument> argumentType;

    protected AnnotationArgumentResolver(Class<? extends Argument> argumentType) {
        this.argumentType = argumentType;
    }

    @Override
    public Class<? extends Argument> getArgumentType() {
        return argumentType;
    }

    @Override
    public Class<RawInput> getInputType() {
        return RawInput.class;
    }

}
