package dev.rollczi.litecommands.chatgpt;

public class ChatGptMessage {

    private final String role;
    private final String content;

    ChatGptMessage(ChatGptRole role, String content) {
        this.role = role.getName();
        this.content = content;
    }

    public ChatGptMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

}
