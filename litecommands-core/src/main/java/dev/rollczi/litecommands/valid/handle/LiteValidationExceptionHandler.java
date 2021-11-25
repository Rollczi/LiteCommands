package dev.rollczi.litecommands.valid.handle;

import dev.rollczi.litecommands.valid.ValidationMessagesService;
import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.valid.ValidationCommandException;

public class LiteValidationExceptionHandler implements ValidationExceptionHandler {

    private final ValidationMessagesService messagesService;

    public LiteValidationExceptionHandler(ValidationMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public void handle(LiteInvocation invocation, ValidationCommandException exception) {
        String message = exception.getMessage() == null
                ? messagesService.getMessage(exception.getValidationInfo(), invocation)
                : exception.getMessage();

        if (message == null || message.isEmpty()) {
            return;
        }

        invocation.sender().sendMessage(message);
    }

}
