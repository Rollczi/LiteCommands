package dev.rollczi.litecommands.join;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.annotation.ArgumentFactory;

public class JoinArgumentFactory implements ArgumentFactory<Join> {

    @Override
    public <T> Argument<T> create(AnnotationHolder<Join, T, ?> holder) {
        Join annotation = holder.getAnnotation();
        return new JoinArgument<>(() -> holder.getName(), holder.getFormat(), annotation.separator(), annotation.limit());
    }

}
