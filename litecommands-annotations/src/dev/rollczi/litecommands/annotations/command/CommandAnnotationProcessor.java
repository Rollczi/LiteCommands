package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.strict.StrictMode;
import dev.rollczi.litecommands.util.LiteCommandsUtil;

import java.util.Arrays;

public class CommandAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker
            .on(Command.class, (annotation, metaHolder) -> {
                if (annotation.strict() != StrictMode.DEFAULT) {
                    metaHolder.meta().put(Meta.STRICT_MODE, annotation.strict());
                }
            })
            .onClass(Command.class, (classType, annotation, builder) -> {
                boolean isNotEmpty = LiteCommandsUtil.checkConsistent(annotation.name(), annotation.aliases());

                if (isNotEmpty) {
                    return builder
                        .routeName(annotation.name())
                        .routeAliases(Arrays.asList(annotation.aliases()));
                }

                throw new IllegalArgumentException("Route name cannot be empty");
            });
    }

}
