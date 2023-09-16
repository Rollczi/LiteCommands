package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentFactory;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.Annotation;

class ArgumentRequirementFactory<SENDER, A extends Annotation> implements RequirementFactory<SENDER, A> {

    private final WrapperRegistry wrapperRegistry;
    private final ParserRegistry<SENDER> parserRegistry;
    private final ArgumentFactory<A> argumentFactory;

    public ArgumentRequirementFactory(WrapperRegistry wrapperRegistry, ParserRegistry<SENDER> parserRegistry, ArgumentFactory<A> argumentFactory) {
        this.parserRegistry = parserRegistry;
        this.wrapperRegistry = wrapperRegistry;
        this.argumentFactory = argumentFactory;
    }

    public ArgumentRequirementFactory(WrapperRegistry wrapperRegistry, ParserRegistry<SENDER> parserRegistry) {
        this(wrapperRegistry, parserRegistry, new DefaultArgumentFactory<>());
    }

    @Override
    public <PARSED> Requirement<SENDER, PARSED> create(AnnotationHolder<A, PARSED, ?> holder) {
        WrapFormat<PARSED, ?> wrapFormat = holder.getFormat();
        Argument<PARSED> argument = argumentFactory.create(holder);
        Wrapper wrapper = wrapperRegistry.getWrappedExpectedFactory(wrapFormat);
        ParserSet<SENDER, PARSED> parserSet = parserRegistry.getParserSet(wrapFormat.getParsedType(), argument.toKey());

        return new ArgumentRequirementImpl<>(argument, wrapper, parserSet);
    }

    private static final class DefaultArgumentFactory<A extends Annotation> implements ArgumentFactory<A> {
        @Override
        public <T> Argument<T> create(AnnotationHolder<A, T, ?> holder) {
            return new SimpleArgument<>(holder);
        }
    }

}
