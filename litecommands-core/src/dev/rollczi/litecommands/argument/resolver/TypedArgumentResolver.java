package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.TypedParser;
import dev.rollczi.litecommands.argument.suggester.TypedSuggester;

@SuppressWarnings("rawtypes")
public abstract class TypedArgumentResolver<SENDER, TYPE, ARG extends Argument<TYPE>> implements
    ArgumentResolverBase<SENDER, TYPE>,
    TypedParser<SENDER, TYPE, ARG>,
    TypedSuggester<SENDER, TYPE, ARG>
{

    private final Class<? extends Argument> argumentType;

    protected TypedArgumentResolver(Class<? extends Argument> argumentType) {
        this.argumentType = argumentType;
    }

    @Override
    public Class<? extends Argument> getArgumentType() {
        return argumentType;
    }

}
