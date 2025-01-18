package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

import dev.rollczi.litecommands.permission.PermissionSet;

public class PermissionAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Permission.class, (annotation, metaHolder) -> {
            metaHolder.meta().listEditor(Meta.PERMISSIONS)
                .add(new PermissionSet(annotation.value()))
                .apply();
        });
    }

}
