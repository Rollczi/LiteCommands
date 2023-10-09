package dev.rollczi.litecommands.chatgpt;

public interface ChatGptPromptWithTopic {

    String getPrompt(String commandStructure, String topic);

}
