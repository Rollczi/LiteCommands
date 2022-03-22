package dev.rollczi.litecommands.bind.basic;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.bind.LiteBind;
import dev.rollczi.litecommands.bind.Parameter;

public final class LiteInvocationBind implements Parameter {

    @Override
    public LiteInvocation apply(LiteInvocation invocation) {
        return invocation;
    }

}
