package dev.rollczi.litecommands.chatgpt;

import dev.rollczi.litecommands.annotations.AnnotationProcessorService;
import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.LiteCommandsProvider;
import dev.rollczi.litecommands.extension.LiteCommandsProviderExtension;
import dev.rollczi.litecommands.chatgpt.annotation.ChatGptAnnotationProcessor;
import dev.rollczi.litecommands.join.JoinArgument;
import dev.rollczi.litecommands.meta.MetaKey;

import java.util.function.UnaryOperator;

public class LiteChatGptExtension<SENDER> implements LiteCommandsProviderExtension<SENDER> {

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
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal) {
        ChatGptClient chatGptClient = new ChatGptClient(settings);
        ChatGptStringSuggester<SENDER> suggester = new ChatGptStringSuggester<>(internal.getScheduler(), chatGptClient, settings);

        builder
            .argument(String.class, JoinArgument.KEY.withKey(ARGUMENT_KEY), suggester);
    }

    @Override
    public void extendCommandsProvider(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal, LiteCommandsProvider<SENDER> provider) {
        if (!(provider instanceof LiteCommandsAnnotations)) {
            return;
        }

        LiteCommandsAnnotations<SENDER> annotations = (LiteCommandsAnnotations<SENDER>) provider;
        AnnotationProcessorService<SENDER> annotationService = annotations.getAnnotationProcessorService();

        annotationService.register(new ChatGptAnnotationProcessor<>());
    }

    public LiteChatGptExtension<SENDER> settings(UnaryOperator<ChatGptSettings> settings) {
        settings.apply(this.settings);
        return this;
    }

}
