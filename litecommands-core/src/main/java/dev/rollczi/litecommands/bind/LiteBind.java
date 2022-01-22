package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.LiteInvocation;

@FunctionalInterface
public interface LiteBind {

    Object apply(LiteInvocation invocation);

}
