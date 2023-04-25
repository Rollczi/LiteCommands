package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.command.ParameterCommandRequirement;
import dev.rollczi.litecommands.annotations.command.ParameterWithAnnotationResolver;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.input.ArgumentParserRegistry;
import dev.rollczi.litecommands.argument.input.ArgumentParserSet;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ArgAnnotationResolver<SENDER> implements ParameterWithAnnotationResolver<SENDER, Arg> {

    private final WrappedExpectedService wrappedExpectedService;
    private final ArgumentParserRegistry<SENDER> argumentParserRegistry;

    public ArgAnnotationResolver(WrappedExpectedService wrappedExpectedService, ArgumentParserRegistry<SENDER> argumentParserRegistry) {
        this.wrappedExpectedService = wrappedExpectedService;
        this.argumentParserRegistry = argumentParserRegistry;
    }

    @Override
    public ParameterCommandRequirement<SENDER, ?> resolve(Parameter parameter, Arg annotation) {
        ParameterArgument<Arg, Object> parameterArgument = ParameterArgument.create(wrappedExpectedService, parameter, annotation);

        return this.resolve(parameterArgument);
    }

    private <A extends Annotation, PARSED, ARGUMENT extends ParameterArgument<A, PARSED>> ParameterCommandRequirement<SENDER, PARSED> resolve(ARGUMENT argument) {
        ArgumentParserSet<SENDER, PARSED> parserSet = argumentParserRegistry.getParserSet(argument.getWrapperFormat().getType(), ArgumentKey.key(argument.getClass()));

        return new ArgArgumentRequirement<>(argument, wrappedExpectedService.getWrappedExpectedFactory(argument.getWrapperFormat()), parserSet);
    }

}
