package dev.rollczi.litecommands.valid.handle;

import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationInfo;
import dev.rollczi.litecommands.valid.messages.MessagesService;
import dev.rollczi.litecommands.LiteInvocation;

public class LiteExecutionResultHandler implements ExecutionResultHandler {

    private final MessagesService messagesService;

    public LiteExecutionResultHandler(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public void handle(ExecutionResult executionResult) {
        if (executionResult.isValid()) {
            return;
        }

        LiteComponent.ContextOfResolving context = executionResult.getLastContext();
        LiteInvocation invocation = context.getInvocation();
        ValidationInfo info = executionResult.getValidInfo();

        if (info == ValidationInfo.INTERNAL_ERROR) {
            invocation.sender().sendMessage(messagesService.getMessage(info, executionResult));
            return;
        }

        String message = executionResult.getValidMessage() == null
                        ? messagesService.getMessage(info, executionResult)
                        : executionResult.getValidMessage();

        if (message == null || message.isEmpty()) {
            return;
        }

        invocation.sender().sendMessage(message);
    }

}
