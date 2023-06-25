package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirement;
import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirementFactory;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ParameterArgumentRequirementFactory<A extends Annotation, SENDER> implements ParameterRequirementFactory<SENDER, A> {

    private final WrapperRegistry wrapperRegistry;
    private final ParserRegistry<SENDER> parserRegistry;
    private final ParameterArgumentFactory<A> parameterArgumentFactory;

    public ParameterArgumentRequirementFactory(WrapperRegistry wrapperRegistry, ParserRegistry<SENDER> parserRegistry, ParameterArgumentFactory<A> parameterArgumentFactory) {
        this.wrapperRegistry = wrapperRegistry;
        this.parserRegistry = parserRegistry;
        this.parameterArgumentFactory = parameterArgumentFactory;
    }

    @Override
    public ParameterRequirement<SENDER, ?> create(Parameter parameter, A annotation) {
        return this.resolve(parameterArgumentFactory.create(wrapperRegistry, parameter, annotation));
    }

    private <PARSED> ParameterRequirement<SENDER, PARSED> resolve(ParameterArgument<A, PARSED> argument) {
        WrapFormat<PARSED, ?> format = argument.getWrapperFormat();
        ParserSet<SENDER, PARSED> parserSet = parserRegistry.getParserSet(format.getParsedType(), argument.toKey());

        for (Parser<SENDER, ?, PARSED> parser : parserSet.getParsers()) {
            ArgumentResolverInfo annotation = parser.getClass().getAnnotation(ArgumentResolverInfo.class);

            if (annotation != null) {
                argument.overrideName(annotation.name());
                break;
            }
        }

        return new ParameterArgumentRequirement<>(argument, wrapperRegistry.getWrappedExpectedFactory(format), parserSet);
    }
}
