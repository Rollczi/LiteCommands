package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.LiteInvocation;

@FunctionalInterface
public interface LiteBind {

    Object apply(LiteInvocation invocation);

}
