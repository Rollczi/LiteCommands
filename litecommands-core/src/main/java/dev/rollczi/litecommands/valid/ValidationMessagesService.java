package dev.rollczi.litecommands.valid;

import dev.rollczi.litecommands.LiteInvocation;

import java.util.EnumMap;
import java.util.function.Function;

public class ValidationMessagesService {

    private final EnumMap<ValidationInfo, Function<LiteInvocation, String>> customMessages = new EnumMap<>(ValidationInfo.class);

    public ValidationMessagesService registerMessage(ValidationInfo validationInfo, String message) {
        customMessages.put(validationInfo, invocation -> message);
        return this;
    }

    public ValidationMessagesService registerMessage(ValidationInfo validationInfo, ValidationInfo.ContextMessage message) {
        customMessages.put(validationInfo, message);
        return this;
    }

    public String getMessage(ValidationInfo validationInfo, LiteInvocation invocation) {
        return customMessages.getOrDefault(validationInfo, validationInfo.getDefaultMessage()).apply(invocation);
    }

}
