package dev.rollczi.litecommands.annotations.varargs;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.argument.resolver.collector.CollectorArgument;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import java.util.Collection;

public class VarargsArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Varargs> {

    public VarargsArgumentProcessor() {
        super(Varargs.class);
    }

    @Override
    public <T> Argument<T> create(AnnotationHolder<Varargs, T, ?> holder) {
        Varargs annotation = holder.getAnnotation();

        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        WrapFormat<T, ?> format = holder.getFormat();
        TypeToken<T> parsedType = format.parsedType();

        if (parsedType.isArray()) {
            return new CollectorArgument<>(name, format, parsedType.getComponentTypeToken(), annotation.delimiter());
        }

        if (parsedType.isInstanceOf(Collection.class)) {
            return new CollectorArgument<>(name, format, parsedType.getParameterized(), annotation.delimiter());
        }

        throw new IllegalArgumentException("@Varargs annotation can be used only with array or collection types. Use e.g. List<String>, String[] or replace the annotation with @Arg.");
    }

}
