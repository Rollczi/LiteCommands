package dev.rollczi.litecommands.bind.basic;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.bind.Parameter;

public final class ArgumentsBind implements Parameter {

    @Override
    public String[] apply(LiteInvocation invocation) {
        return invocation.arguments();
    }

}
