package dev.rollczi.litecommands.inject.basic;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.inject.LiteBind;

public final class ArgumentsBind implements LiteBind {

    @Override
    public String[] apply(LiteInvocation invocation) {
        return invocation.arguments();
    }

}
