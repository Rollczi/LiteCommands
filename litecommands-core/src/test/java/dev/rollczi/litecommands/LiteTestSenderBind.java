package dev.rollczi.litecommands;

import dev.rollczi.litecommands.bind.Parameter;

public class LiteTestSenderBind implements Parameter {

    @Override
    public Object apply(LiteInvocation invocation) {
        return new LiteTestSender();
    }

}
