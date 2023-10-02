package dev.rollczi.litecommands.chatgpt;

import dev.rollczi.litecommands.annotations.AnnotationProcessorService;
import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.builder.LiteCommandsProvider;
import dev.rollczi.litecommands.builder.extension.LiteCommandsProviderExtension;
import dev.rollczi.litecommands.chatgpt.annotation.ChatGpt;
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
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern) {
        ChatGptClient chatGptClient = new ChatGptClient(settings);
        ChatGptStringSuggester<SENDER> suggester = new ChatGptStringSuggester<>(pattern.getScheduler(), chatGptClient, settings);

        builder
            .argumentSuggester(String.class, JoinArgument.KEY.withKey(ARGUMENT_KEY), suggester);
    }

    @Override
    public void extendCommandsProvider(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern, LiteCommandsProvider<SENDER> provider) {
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
