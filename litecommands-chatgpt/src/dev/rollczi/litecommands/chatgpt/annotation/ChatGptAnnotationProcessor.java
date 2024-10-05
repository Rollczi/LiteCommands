package dev.rollczi.litecommands.chatgpt.annotation;

import dev.rollczi.litecommands.annotations.argument.profile.ProfileAnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.chatgpt.ChatGptArgumentProfile;
import java.lang.reflect.Parameter;

public class ChatGptAnnotationProcessor<SENDER> extends ProfileAnnotationProcessor<SENDER, ChatGpt, ChatGptArgumentProfile> {

    public ChatGptAnnotationProcessor() {
        super(ChatGpt.class);
    }

    @Override
    protected ChatGptArgumentProfile createProfile(Parameter parameter, ChatGpt annotation, Argument<?> argument) {
        return new ChatGptArgumentProfile(annotation.topic());
    }

}
