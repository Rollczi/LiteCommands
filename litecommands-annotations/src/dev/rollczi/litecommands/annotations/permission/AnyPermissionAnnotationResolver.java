package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.permission.PermissionSection;

import java.util.Arrays;

public class AnyPermissionAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(AnyPermission.class, (annotation, metaHolder) -> {
            metaHolder.meta().listEditor(Meta.PERMISSIONS)
                .add(PermissionSection.of(Arrays.asList(annotation.value()), annotation.type()))
                .apply();
        });
    }

}
