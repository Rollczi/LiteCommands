package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class PermissionAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Permission.class, (annotation, metaHolder) -> {
            metaHolder.meta().setEditor(Meta.PERMISSIONS)
                .add(new LinkedHashSet<>(Arrays.asList(annotation.value())))
                .apply();
        });
    }

}
