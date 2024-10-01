package dev.rollczi.litecommands.annotations.varargs;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.collector.CollectionArgumentProfile;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;
import java.util.Collection;

public class VarargsArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Varargs> {

    public VarargsArgumentProcessor() {
        super(Varargs.class);
    }

    @Override
    protected Requirement<?> create(AnnotationHolder<Varargs, ?> holder) {
        Varargs annotation = holder.getAnnotation();

        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        TypeToken<?> collectorType = holder.getType();

        if (collectorType.isArray()) {
            TypeToken<?> componentType = collectorType.getComponentTypeToken();

            return Argument.profiled(name, collectorType, new CollectionArgumentProfile(componentType, annotation.delimiter()));
        }

        if (collectorType.isInstanceOf(Collection.class)) {
            TypeToken<?> elementType = collectorType.getParameterized();

            return Argument.profiled(name, collectorType, new CollectionArgumentProfile(elementType, annotation.delimiter()));
        }

        throw new IllegalArgumentException("@Varargs annotation can be used only with array or collection types. Use e.g. List<String>, String[] or replace the annotation with @Arg.");
    }

}
