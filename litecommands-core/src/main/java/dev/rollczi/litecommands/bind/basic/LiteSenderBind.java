package dev.rollczi.litecommands.bind.basic;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.bind.Parameter;

public final class LiteSenderBind implements Parameter {

    @Override
    public Object apply(LiteInvocation invocation) {
        return invocation.sender();
    }

}
