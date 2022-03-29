package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.LiteInvocation;

@FunctionalInterface
public interface Parameter {

    Object apply(LiteInvocation invocation);

}
