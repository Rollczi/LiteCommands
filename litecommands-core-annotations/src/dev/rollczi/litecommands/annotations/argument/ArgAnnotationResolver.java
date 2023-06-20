package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.command.ParameterRequirement;
import dev.rollczi.litecommands.annotations.command.ParameterWithAnnotationResolver;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.reflect.Parameter;

public class ArgAnnotationResolver<SENDER> implements ParameterWithAnnotationResolver<SENDER, Arg> {

    private final WrapperRegistry wrapperRegistry;
    private final ParserRegistry<SENDER> parserRegistry;

    public ArgAnnotationResolver(WrapperRegistry wrapperRegistry, ParserRegistry<SENDER> parserRegistry) {
        this.wrapperRegistry = wrapperRegistry;
        this.parserRegistry = parserRegistry;
    }

    @Override
    public ParameterRequirement<SENDER, ?> resolve(Parameter parameter, Arg annotation) {
        ArgParameterArgument<?> parameterArgument = ArgParameterArgument.createArg(wrapperRegistry, parameter, annotation);

        return this.resolve(parameterArgument);
    }

    private <PARSED> ParameterRequirement<SENDER, PARSED> resolve(ArgParameterArgument<PARSED> argument) {
        WrapFormat<PARSED, ?> format = argument.getWrapperFormat();
        ParserSet<SENDER, PARSED> parserSet = parserRegistry.getParserSet(format.getParsedType(), argument.toKey());

        for (Parser<SENDER, ?, PARSED> parser : parserSet.getParsers()) {
            ArgumentResolverInfo annotation = parser.getClass().getAnnotation(ArgumentResolverInfo.class);

            if (annotation != null) {
                argument.overrideName(annotation.name());
                break;
            }
        }

        return new ArgArgumentRequirement<>(argument, wrapperRegistry.getWrappedExpectedFactory(format), parserSet);
    }

}
