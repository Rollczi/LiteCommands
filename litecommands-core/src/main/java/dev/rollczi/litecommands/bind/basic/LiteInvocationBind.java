package dev.rollczi.litecommands.bind.basic;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.bind.LiteBind;

public final class LiteInvocationBind implements LiteBind {

    @Override
    public LiteInvocation apply(LiteInvocation invocation) {
        return invocation;
    }

}
