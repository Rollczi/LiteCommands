package dev.rollczi.litecommands.chatgpt;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.chatgpt.annotation.ChatGptAnnotationProcessor;
import dev.rollczi.litecommands.configurator.LiteConfigurator;
import dev.rollczi.litecommands.extension.LiteExtension;

import java.util.function.UnaryOperator;

public class LiteChatGptExtension<SENDER> implements LiteExtension<SENDER, ChatGptSettings> {

    private final ChatGptSettings settings = new ChatGptSettings();

    public LiteChatGptExtension() {
    }

    public LiteChatGptExtension(UnaryOperator<ChatGptSettings> settings) {
        settings.apply(this.settings);
    }

    public LiteChatGptExtension(String apiKey, ChatGptModel model, double temperature) {
        this.settings
            .apiKey(apiKey)
            .model(model)
            .temperature(temperature);
    }

    @Override
    public void configure(LiteConfigurator<ChatGptSettings> configurer) {
        configurer.configure(settings);
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal) {
        ChatGptClient chatGptClient = settings.createChatGptClient();
        ChatGptStringArgumentResolver<SENDER> argumentResolver = new ChatGptStringArgumentResolver<>(internal.getScheduler(), chatGptClient, settings);

        builder
            .argument(String.class, ChatGptArgumentProfile.NAMESPACE, argumentResolver)
            .annotations(configuration -> configuration
                .processor(new ChatGptAnnotationProcessor<>())
            );
    }

    @Deprecated
    public LiteChatGptExtension<SENDER> settings(UnaryOperator<ChatGptSettings> settings) {
        settings.apply(this.settings);
        return this;
    }

}
