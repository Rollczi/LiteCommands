package dev.rollczi.litecommands.chatgpt.annotation;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.chatgpt.LiteChatGptExtension;
import dev.rollczi.litecommands.meta.Meta;

public class ChatGptAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onParameterRequirement(ChatGpt.class, (parameter, metaHolder, builder, requirement) -> {
            requirement.meta()
                .edit(Meta.ARGUMENT_KEY, argumentKey -> argumentKey.withKey(LiteChatGptExtension.ARGUMENT_KEY))
                .put(LiteChatGptExtension.ARGUMENT_TOPIC, metaHolder.getAnnotation().topic());
        });
    }

}
