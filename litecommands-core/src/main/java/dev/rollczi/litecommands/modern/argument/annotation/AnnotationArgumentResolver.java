package dev.rollczi.litecommands.modern.argument.annotation;

import dev.rollczi.litecommands.modern.argument.invocation.ArgumentParser;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResolver;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResultCollector;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResultPreparedCollector;

import java.lang.annotation.Annotation;

public class AnnotationArgumentResolver<SENDER, ARGUMENT extends Annotation, TYPE> implements ArgumentResolver<SENDER, ARGUMENT, TYPE, AnnotationArgumentContext<ARGUMENT, TYPE>> {

    private final ArgumentParser<SENDER, ARGUMENT, TYPE, AnnotationArgumentContext<ARGUMENT, TYPE>> parser;

    public AnnotationArgumentResolver(ArgumentParser<SENDER, ARGUMENT, TYPE, AnnotationArgumentContext<ARGUMENT, TYPE>> parser) {
        this.parser = parser;
    }

    @Override
    public ArgumentResultCollector<SENDER> resolve(AnnotationArgumentContext<ARGUMENT, TYPE> context, ArgumentResultPreparedCollector<SENDER, ARGUMENT, TYPE, AnnotationArgumentContext<ARGUMENT, TYPE>> collector) {
        return collector.collect(this.parser.getArgumentCount().getMin(), parser);
    }

}
