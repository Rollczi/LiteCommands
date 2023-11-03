package dev.rollczi.litecommands.jda.permission;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;

public class JDACommandAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(DiscordPermission.class, (annotation, metaHolder) -> {
            metaHolder.meta()
                .listEditor(DiscordPermission.META_KEY)
                .addAll(annotation.value())
                .apply();
        });
    }

}
