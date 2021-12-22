package dev.rollczi.litecommands.valid.messages;

import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.valid.ValidationInfo;

import java.util.EnumMap;

public class MessagesService {

    private final EnumMap<ValidationInfo, ContextualMessage> customMessages = new EnumMap<>(ValidationInfo.class);
    private UseSchemeFormatting useSchemeFormatting = UseSchemeFormatting.NORMAL;

    public MessagesService registerMessage(ValidationInfo validationInfo, ContextualMessage message) {
        customMessages.put(validationInfo, message);
        return this;
    }

    public String getMessage(ValidationInfo validationInfo, ExecutionResult result) {
        return customMessages.getOrDefault(validationInfo, validationInfo.getDefaultMessage()).message(result, useSchemeFormatting);
    }

    public void setUseSchemeFormatting(UseSchemeFormatting useSchemeFormatting) {
        this.useSchemeFormatting = useSchemeFormatting;
    }

    public UseSchemeFormatting getUseSchemeFormatting() {
        return useSchemeFormatting;
    }

}
