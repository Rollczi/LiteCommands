package dev.rollczi.litecommands.valid.handle;

import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationInfo;
import dev.rollczi.litecommands.valid.ValidationMessagesService;
import dev.rollczi.litecommands.LiteInvocation;

public class LiteExecutionResultHandler implements ExecutionResultHandler {

    private final ValidationMessagesService messagesService;

    public LiteExecutionResultHandler(ValidationMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public void handle(ExecutionResult executionResult, LiteComponent.ContextOfResolving context) {
        if (executionResult.isValid()) {
            return;
        }

        LiteInvocation invocation = context.getInvocation();
        ValidationInfo info = executionResult.getValidInfo();

        if (info == ValidationInfo.INTERNAL_ERROR) {
            invocation.sender().sendMessage("&cInternal error");
            return;
        }

        String message = executionResult.getValidMessage() == null
                        ? messagesService.getMessage(info, invocation)
                        : executionResult.getValidMessage();


        if (message == null || message.isEmpty()) {
            return;
        }

        invocation.sender().sendMessage(message);
    }

}
