package dev.rollczi.litecommands.join;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.argument.ArgumentFactory;

public class JoinArgumentFactory implements ArgumentFactory<Join> {

    @Override
    public <T> Argument<T> create(AnnotationHolder<Join, T, ?> holder) {
        return new JoinArgument<>(holder);
    }

}
