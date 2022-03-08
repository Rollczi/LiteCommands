package dev.rollczi.litecommands.scheme;

import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.valid.messages.LiteMessage;
import dev.rollczi.litecommands.valid.messages.MessageInfoContext;
import dev.rollczi.litecommands.valid.messages.UseSchemeFormatting;

public class TestSchemeMessage implements LiteMessage {

    private final UseSchemeFormatting schemeFormatting;

    public TestSchemeMessage(UseSchemeFormatting schemeFormatting) {
        this.schemeFormatting = schemeFormatting;
    }

    public String message(ExecutionResult executionResult) {
        return this.message(executionResult, this.schemeFormatting);
    }

    @Override
    public String message(MessageInfoContext messageInfoContext) {
        return messageInfoContext.getUseScheme();
    }

}
