package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.argument.resolver.array.ArrayArgument;
import dev.rollczi.litecommands.wrapper.WrapFormat;

public class ArgArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Arg> {

    public ArgArgumentProcessor() {
        super(Arg.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Argument<T> create(AnnotationHolder<Arg, T, ?> holder) {
        Arg annotation = holder.getAnnotation();

        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        WrapFormat<T, ?> format = holder.getFormat();
        Class<T> parsedType = format.getParsedType();

        if (parsedType.isArray()) {
            return (Argument<T>) new ArrayArgument(name, (WrapFormat<Object, ?>) format, parsedType.getComponentType(), parsedType);
        }

        return new SimpleArgument<>(name, format);
    }

}
