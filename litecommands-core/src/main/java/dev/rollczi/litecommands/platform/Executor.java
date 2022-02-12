package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.valid.ValidationCommandException;

public interface Executor {

    ExecutionResult execute(LiteInvocation invocation) throws ValidationCommandException;

}
