package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

public class PermissionAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Permission.class, (annotation, metaHolder) -> {
            metaHolder.meta().listEditor(Meta.PERMISSIONS)
                .addAll(annotation.value())
                .apply();
        });
    }

}
