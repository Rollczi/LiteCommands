package dev.rollczi.litecommands.chatgpt;

public interface ChatGptClient {

    String chat(String message);

    String chat(String context, String message);

    String chat(ChatGptMessage... messages);

}
