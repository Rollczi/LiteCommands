package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.valid.ValidationCommandException;

public interface Executor {

    void execute(LiteInvocation invocation) throws ValidationCommandException;

}
