package dev.rollczi.litecommands.chatgpt;

import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.join.JoinProfile;
import dev.rollczi.litecommands.reflect.type.TypeToken;

public class ChatGptArgument extends SimpleArgument<String> {

    public ChatGptArgument(String name, String topic) {
        super(name, TypeToken.of(String.class), false);
        this.withProfile(new ChatGptArgumentProfile(topic));
        this.withProfile(new JoinProfile());
    }

    public ChatGptArgument(String name) {
        this(name, "");
    }

}
