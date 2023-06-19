package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.command.ParameterRequirement;
import dev.rollczi.litecommands.annotations.command.ParameterWithAnnotationResolver;
import dev.rollczi.litecommands.argument.parser.ArgumentParserRegistry;
import dev.rollczi.litecommands.argument.parser.ArgumentParserSet;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ArgAnnotationResolver<SENDER> implements ParameterWithAnnotationResolver<SENDER, Arg> {

    private final WrapperRegistry wrapperRegistry;
    private final ArgumentParserRegistry<SENDER> argumentParserRegistry;

    public ArgAnnotationResolver(WrapperRegistry wrapperRegistry, ArgumentParserRegistry<SENDER> argumentParserRegistry) {
        this.wrapperRegistry = wrapperRegistry;
        this.argumentParserRegistry = argumentParserRegistry;
    }

    @Override
    public ParameterRequirement<SENDER, ?> resolve(Parameter parameter, Arg annotation) {
        ParameterArgument<Arg, Object> parameterArgument = ArgParameterArgument.createArg(wrapperRegistry, parameter, annotation);

        return this.resolve(parameterArgument);
    }

    private <A extends Annotation, PARSED, ARGUMENT extends ParameterArgument<A, PARSED>> ParameterRequirement<SENDER, PARSED> resolve(ARGUMENT argument) {
        WrapFormat<PARSED, ?> format = argument.getWrapperFormat();
        ArgumentParserSet<SENDER, PARSED> parserSet = argumentParserRegistry.getParserSet(format.getParsedType(), argument.toKey());

        return new ArgArgumentRequirement<>(argument, wrapperRegistry.getWrappedExpectedFactory(format), parserSet);
    }

}
