package dev.rollczi.litecommands.annotations.argument.collector;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.argument.resolver.collector.CollectorArgument;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import java.util.Collection;

public class ArgCollectionArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Arg> {

    public ArgCollectionArgumentProcessor() {
        super(Arg.class);
    }

    @Override
    public <T> Argument<T> create(AnnotationHolder<Arg, T, ?> holder) {
        Arg annotation = holder.getAnnotation();

        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        WrapFormat<T, ?> format = holder.getFormat();
        TypeToken<T> parsedType = format.parsedType();

        if (parsedType.isArray()) {
            return new CollectorArgument<>(name, format, parsedType.getComponentTypeToken(), RawCommand.COMMAND_SEPARATOR);
        }

        if (parsedType.isInstanceOf(Collection.class)) {
            return new CollectorArgument<>(name, format, parsedType.getParameterized(), RawCommand.COMMAND_SEPARATOR);
        }

        return new SimpleArgument<>(name, format);
    }

}
