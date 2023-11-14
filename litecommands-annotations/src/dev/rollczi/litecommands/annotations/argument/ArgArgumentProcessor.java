package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.SimpleAnnotatedArgument;
import dev.rollczi.litecommands.argument.SimpleArgument;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ArgArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Arg> {

    public ArgArgumentProcessor() {
        super(Arg.class);
    }

    @Override
    public <T> Argument<T> create(AnnotationHolder<Arg, T, ?> holder) {
        Arg annotation = holder.getAnnotation();

        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        Annotation[] annotations = holder.getAnnotations();
        return annotations.length > 0
            ? new SimpleAnnotatedArgument<>(name, holder.getFormat(), Arrays.stream(annotations).collect(Collectors.toMap(Annotation::annotationType, a -> a)))
            : new SimpleArgument<>(name, holder.getFormat());
    }

}
