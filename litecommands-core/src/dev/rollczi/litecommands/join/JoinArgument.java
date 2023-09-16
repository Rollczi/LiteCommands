package dev.rollczi.litecommands.join;

import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.argument.SimpleArgument;

class JoinArgument<EXPECTED> extends SimpleArgument<Join, EXPECTED> {

    public JoinArgument(AnnotationHolder<Join, EXPECTED, ?> holder) {
        super(holder);
    }

}
