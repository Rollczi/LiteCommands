package dev.rollczi.litecommands.valid.messages;

import dev.rollczi.litecommands.component.ExecutionResult;

@FunctionalInterface
public interface ContextualMessage {

    String message(ExecutionResult result, UseSchemeFormatting formatting);

}