package dev.rollczi.litecommands.valid.messages;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.ExecutionResult;

import java.util.List;

public class MessageInfoContext {

    private final ExecutionResult executionResult;
    private final String useScheme;

    public MessageInfoContext(ExecutionResult executionResult, String useScheme) {
        this.executionResult = executionResult;
        this.useScheme = useScheme;
    }

    public MessageInfoContext(ExecutionResult executionResult) {
        this.executionResult = executionResult;
        this.useScheme = "/" + executionResult.getLastContext().getInvocation().name();
    }

    public LiteInvocation getInvocation() {
        return executionResult.getLastContext().getInvocation();
    }

    public List<String> getMissingPermissions() {
        return executionResult.getMissingPermissions();
    }

    public ExecutionResult getExecutionResult() {
        return executionResult;
    }

    public String getUseScheme() {
        return useScheme;
    }

}
