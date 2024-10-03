package dev.rollczi.litecommands.chatgpt;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.priority.PriorityLevel;

public class ChatGptArgumentProfile implements ArgumentProfile<ChatGptArgumentProfile> {

    public static final ArgumentProfileNamespace<ChatGptArgumentProfile> NAMESPACE = ArgumentProfileNamespace.of("profile:chatgpt", ChatGptArgumentProfile.class);

    private final String topic;

    public ChatGptArgumentProfile(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public ArgumentProfileNamespace<ChatGptArgumentProfile> getNamespace() {
        return NAMESPACE;
    }

    @Override
    public PriorityLevel getPriority() {
        return PriorityLevel.HIGH;
    }

}
