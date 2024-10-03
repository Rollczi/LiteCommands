package dev.rollczi.litecommands.chatgpt;

public class MockChatGptClient implements ChatGptClient {

    @Override
    public String chat(String message) {
        return "'Hello, World!'";
    }

    @Override
    public String chat(String context, String message) {
        return "'Hello, World!'";
    }

    @Override
    public String chat(ChatGptMessage... messages) {
        return "'Hello, World!'";
    }

}
