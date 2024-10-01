package dev.rollczi.litecommands.annotations.argument.collection;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.collector.CollectionArgumentProfile;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;
import java.util.Collection;

public class ArgCollectionArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Arg> {

    public ArgCollectionArgumentProcessor() {
        super(Arg.class);
    }

    @Override
    protected Requirement<?> create(AnnotationHolder<Arg, ?> holder) {
        Arg annotation = holder.getAnnotation();
        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        TypeToken<?> collectorType = holder.getType();

        if (collectorType.isArray()) {
            TypeToken<?> componentType = collectorType.getComponentTypeToken();

            return Argument.profiled(name, collectorType, new CollectionArgumentProfile(componentType, RawCommand.COMMAND_SEPARATOR));
        }

        if (collectorType.isInstanceOf(Collection.class)) {
            TypeToken<?> elementType = collectorType.getParameterized();

            return Argument.profiled(name, collectorType, new CollectionArgumentProfile(elementType, RawCommand.COMMAND_SEPARATOR));
        }

        return Argument.of(name, collectorType);
    }

}
