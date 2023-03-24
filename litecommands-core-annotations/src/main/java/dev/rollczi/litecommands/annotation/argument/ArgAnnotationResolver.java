package dev.rollczi.litecommands.annotation.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentParser;
import dev.rollczi.litecommands.argument.ArgumentResolverRegistry;
import dev.rollczi.litecommands.annotation.command.ParameterPreparedArgument;
import dev.rollczi.litecommands.annotation.command.ParameterWithAnnotationResolver;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ArgAnnotationResolver<SENDER> implements ParameterWithAnnotationResolver<SENDER, Arg> {

    private final WrappedExpectedService wrappedExpectedService;
    private final ArgumentResolverRegistry<SENDER> argumentResolverRegistry;

    public ArgAnnotationResolver(WrappedExpectedService wrappedExpectedService, ArgumentResolverRegistry<SENDER> argumentResolverRegistry) {
        this.wrappedExpectedService = wrappedExpectedService;
        this.argumentResolverRegistry = argumentResolverRegistry;
    }

    @Override
    public ParameterPreparedArgument<SENDER, ?> resolve(Parameter parameter, Arg annotation) {
        ParameterArgument<Arg, Object> parameterArgument = ParameterArgument.create(wrappedExpectedService, parameter, annotation);

        return this.resolve(parameterArgument);
    }

    private <A extends Annotation, E, ARGUMENT extends ParameterArgument<A, E>> ParameterPreparedArgument<SENDER, E> resolve(ARGUMENT argument) {
        ArgumentParser<SENDER, E, Argument<E>> parser = argumentResolverRegistry.getResolver(ArgumentResolverRegistry.IndexKey.from(argument))
            .orElseThrow(() -> new IllegalArgumentException("Cannot find resolver for " + argument));

        return new ArgPreparedArgument<>(argument, parser, (invocation, arguments) -> parser.parse(invocation, argument, arguments));
    }

}
