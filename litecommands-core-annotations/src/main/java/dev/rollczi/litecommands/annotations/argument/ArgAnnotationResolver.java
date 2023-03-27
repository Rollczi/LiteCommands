package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.command.ParameterPreparedArgument;
import dev.rollczi.litecommands.annotations.command.ParameterWithAnnotationResolver;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentParser;
import dev.rollczi.litecommands.argument.ArgumentService;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ArgAnnotationResolver<SENDER> implements ParameterWithAnnotationResolver<SENDER, Arg> {

    private final WrappedExpectedService wrappedExpectedService;
    private final ArgumentService<SENDER> argumentService;

    public ArgAnnotationResolver(WrappedExpectedService wrappedExpectedService, ArgumentService<SENDER> argumentService) {
        this.wrappedExpectedService = wrappedExpectedService;
        this.argumentService = argumentService;
    }

    @Override
    public ParameterPreparedArgument<SENDER, ?> resolve(Parameter parameter, Arg annotation) {
        ParameterArgument<Arg, Object> parameterArgument = ParameterArgument.create(wrappedExpectedService, parameter, annotation);

        return this.resolve(parameterArgument);
    }

    private <A extends Annotation, E, ARGUMENT extends ParameterArgument<A, E>> ParameterPreparedArgument<SENDER, E> resolve(ARGUMENT argument) {
        ArgumentParser<SENDER, E, Argument<E>> parser = argumentService.getResolver(ArgumentService.IndexKey.from(argument))
            .orElseThrow(() -> new IllegalArgumentException("Cannot find resolver for " + argument));

        return new ArgPreparedArgument<>(argument, parser, (invocation, arguments) -> parser.parse(invocation, argument, arguments), wrappedExpectedService.getWrappedExpectedFactory(argument.getWrapperFormat()));
    }

}
