package dev.rollczi.litecommands;

import dev.rollczi.litecommands.bind.LiteBind;

public class LiteTestSenderBind implements LiteBind {

    @Override
    public Object apply(LiteInvocation invocation) {
        return new LiteTestSender();
    }

}
