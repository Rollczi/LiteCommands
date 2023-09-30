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

public class LiteChatGptExtension<SENDER> implements LiteCommandsProviderExtension<SENDER> {

    @Override
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern) {
        ChatGptClient chatGptClient = new ChatGptClient("w", ChatGptModel.GPT_4, 1.0);

        builder
            .argument(String.class, JoinArgument.KEY.withKey(ChatGpt.ARGUMENT_KEY), new ChatGptStringSuggester<>(chatGptClient, pattern.getScheduler()));
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
}
