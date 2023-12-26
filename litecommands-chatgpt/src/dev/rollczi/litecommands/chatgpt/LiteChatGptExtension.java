package dev.rollczi.litecommands.chatgpt;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.chatgpt.annotation.ChatGptAnnotationProcessor;
import dev.rollczi.litecommands.configurator.LiteConfigurator;
import dev.rollczi.litecommands.extension.LiteExtension;
import dev.rollczi.litecommands.join.JoinArgument;
import dev.rollczi.litecommands.meta.MetaKey;

import java.util.function.UnaryOperator;

public class LiteChatGptExtension<SENDER> implements LiteExtension<SENDER, ChatGptSettings> {

    public static final String ARGUMENT_KEY = "chat-gpt";
    public static final MetaKey<String> ARGUMENT_TOPIC = MetaKey.of("chat-gpt-topic", String.class, "");

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
        ChatGptClient chatGptClient = new ChatGptClient(settings);
        ChatGptStringSuggester<SENDER> suggester = new ChatGptStringSuggester<>(internal.getScheduler(), chatGptClient, settings);

        builder
            .argument(String.class, JoinArgument.KEY.withKey(ARGUMENT_KEY), suggester)
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
