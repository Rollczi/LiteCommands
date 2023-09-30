package dev.rollczi.litecommands.chatgpt;

class ChatGptMessage {

    private final String role;
    private final String content;

    public ChatGptMessage(ChatGptRole role, String content) {
        this.role = role.getName();
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

}
