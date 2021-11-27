package dev.rollczi.litecommands.valid.handle;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.component.LiteComponent;

public interface ExecutionResultHandler {

    void handle(ExecutionResult executionResult, LiteComponent.ContextOfResolving context);

}
