package dev.rollczi.litecommands.chatgpt;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.chatgpt.annotation.ChatGpt;
import dev.rollczi.litecommands.scheduler.SchedulerSameThreadImpl;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

class ChatGptArgumentTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder
        .scheduler(new SchedulerSameThreadImpl())
        .extension(new LiteChatGptExtension<>(), configuration -> configuration
            .chatGptClient(() -> new MockChatGptClient())
            .tokensLimit(0, 256)
        );

    @Command(name = "chatgpt")
    static class ChatGptCommand {
        @Execute
        String executeMessage(@Join @ChatGpt String message) {
            return message;
        }
    }

    @Test
    void testChatGptMessage() {
        platform.execute("chatgpt Hello, World!")
            .assertSuccess("Hello, World!");

        platform.suggest("chatgpt ")
            .assertSuggest("Hello, World!");
    }

}