package dev.rollczi.litecommands.bind.basic;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.bind.LiteBind;

public final class ArgumentsBind implements LiteBind {

    @Override
    public String[] apply(LiteInvocation invocation) {
        return invocation.arguments();
    }

}
