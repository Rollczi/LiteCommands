package dev.rollczi.litecommands.bind.basic;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.bind.LiteBind;

public final class LiteSenderBind implements LiteBind {

    @Override
    public Object apply(LiteInvocation invocation) {
        return invocation.sender();
    }

}
