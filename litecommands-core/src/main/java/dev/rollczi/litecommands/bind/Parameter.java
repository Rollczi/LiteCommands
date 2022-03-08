package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.LiteInvocation;

@FunctionalInterface
public interface Parameter<T> {

    Object apply(LiteInvocation invocation);

}
