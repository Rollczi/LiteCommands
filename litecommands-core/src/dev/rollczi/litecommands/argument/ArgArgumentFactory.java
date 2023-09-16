package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.annotation.AnnotationHolder;

public class ArgArgumentFactory implements ArgumentFactory<Arg> {

    @Override
    public <T> Argument<T> create(AnnotationHolder<Arg, T, ?> holder) {
        Arg annotation = holder.getAnnotation();

        return new SimpleArgument<>(AnnotationHolder.of(annotation, holder.getFormat(), () -> {
            String name = annotation.value();

            if (!name.isEmpty()) {
                return name;
            }

            return holder.getName();
        }));
    }
}
