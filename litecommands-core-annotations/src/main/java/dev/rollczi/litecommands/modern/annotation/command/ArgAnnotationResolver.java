package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.annotation.argument.Arg;
import dev.rollczi.litecommands.modern.annotation.argument.ParameterArgument;
import dev.rollczi.litecommands.modern.annotation.argument.ParameterArgumentFactory;
import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentParser;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ArgAnnotationResolver<SENDER> implements ArgumentAnnotationResolver<SENDER, Arg> {

    private final ParameterArgumentFactory parameterArgumentFactory;
    private final ArgumentResolverRegistry<SENDER> argumentResolverRegistry;

    public ArgAnnotationResolver(ParameterArgumentFactory parameterArgumentFactory, ArgumentResolverRegistry<SENDER> argumentResolverRegistry) {
        this.parameterArgumentFactory = parameterArgumentFactory;
        this.argumentResolverRegistry = argumentResolverRegistry;
    }

    @Override
    public ParameterPreparedArgument<SENDER, ?> resolve(Parameter parameter, Arg annotation) {
        ParameterArgument<Arg, Object> parameterArgument = parameterArgumentFactory.create(parameter, annotation);

        return resolve(parameterArgument);
    }

    private <A extends Annotation, E, ARGUMENT extends ParameterArgument<A, E>> ParameterPreparedArgument<SENDER, E> resolve(ARGUMENT argument) {
        ArgumentParser<SENDER, E, Argument<E>> parser = argumentResolverRegistry.getResolver(ArgumentResolverRegistry.IndexKey.from(argument))
            .orElseThrow(() -> new IllegalArgumentException("Cannot find resolver for " + argument));

        return new ArgPreparedArgument<>(argument, parser, (invocation, arguments) -> parser.parse(invocation, argument, arguments));
    }

}
