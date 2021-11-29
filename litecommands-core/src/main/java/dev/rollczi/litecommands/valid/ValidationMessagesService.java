package dev.rollczi.litecommands.valid;

import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.component.LiteComponent;

import java.util.EnumMap;

public class ValidationMessagesService {

    private final EnumMap<ValidationInfo, ValidationInfo.ContextMessage> customMessages = new EnumMap<>(ValidationInfo.class);

    public ValidationMessagesService registerMessage(ValidationInfo validationInfo, String message) {
        customMessages.put(validationInfo, (context, result) -> message);
        return this;
    }

    public ValidationMessagesService registerMessage(ValidationInfo validationInfo, ValidationInfo.ContextMessage message) {
        customMessages.put(validationInfo, message);
        return this;
    }

    public String getMessage(ValidationInfo validationInfo, LiteComponent.ContextOfResolving context, ExecutionResult result) {
        return customMessages.getOrDefault(validationInfo, validationInfo.getDefaultMessage()).message(context, result);
    }

}
