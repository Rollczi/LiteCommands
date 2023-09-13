package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.annotations.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.processor.AnnotationProcessor;
import dev.rollczi.litecommands.util.LiteCommandsUtil;

import java.util.Arrays;

public class CommandAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onAnnotatedClass(Command.class, (instance, annotation, builder) -> {
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