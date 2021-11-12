package dev.rollczi.litecommands.inject.basic;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.inject.LiteBind;

public final class OriginalSenderBind implements LiteBind {

    @Override
    public Object apply(LiteInvocation invocation) {
        return invocation.sender().getSender();
    }

}
