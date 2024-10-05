package dev.rollczi.litecommands.chatgpt;

public class MockChatGptClient implements ChatGptClient {

    @Override
    public String chat(String message) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String chat(String context, String message) {
        return "'" + context + "'";
    }

    @Override
    public String chat(ChatGptMessage... messages) {
        throw new UnsupportedOperationException("Not implemented");
    }

}
