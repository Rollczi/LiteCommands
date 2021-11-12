package dev.rollczi.litecommands.inject.basic;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.inject.LiteBind;

public final class LiteInvocationBind implements LiteBind {

    @Override
    public LiteInvocation apply(LiteInvocation invocation) {
        return invocation;
    }

}
