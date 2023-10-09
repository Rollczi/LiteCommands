package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

public class PermissionsAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Permissions.class, (annotation, metaHolder) -> {
            Meta meta = metaHolder.meta();

            for (Permission permissionAnnotation : annotation.value()) {
                meta.listEditor(Meta.PERMISSIONS)
                    .addAll(permissionAnnotation.value())
                    .apply();
            }
        });
    }

}
