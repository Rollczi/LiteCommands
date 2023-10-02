package dev.rollczi.litecommands.chatgpt;

class ChatGptRole {

    public static final ChatGptRole SYSTEM = new ChatGptRole("system");
    public static final ChatGptRole USER = new ChatGptRole("user");

    private final String name;

    public ChatGptRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
