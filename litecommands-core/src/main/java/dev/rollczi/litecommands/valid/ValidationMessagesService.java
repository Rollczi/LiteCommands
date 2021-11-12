package dev.rollczi.litecommands.valid;

import java.util.EnumMap;

public class ValidationMessagesService {

    private final EnumMap<ValidationInfo, String> customMessages = new EnumMap<>(ValidationInfo.class);

    public ValidationMessagesService registerMessage(ValidationInfo validationInfo, String message) {
        customMessages.put(validationInfo, message);
        return this;
    }

    public String getMessage(ValidationInfo validationInfo) {
        return customMessages.getOrDefault(validationInfo, validationInfo.getDefaultMessage());
    }

}
