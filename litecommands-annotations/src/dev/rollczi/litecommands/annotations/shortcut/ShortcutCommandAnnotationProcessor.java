package dev.rollczi.litecommands.annotations.shortcut;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.util.LiteCommandsUtil;
import java.util.Arrays;

public class ShortcutCommandAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onExecutorStructure(
            Execute.class,
            (executeAnnotation, builder, executorBuilder) -> invoker.onExecutorStructure(
                Shortcut.class,
                (shortAnnotation, shortBuilder, shortExecutorBuilder) -> resolve(executeAnnotation, shortAnnotation, builder)
            )
        );
    }

    private CommandBuilder<SENDER> resolve(Execute executeAnnotation, Shortcut shortAnnotation, CommandBuilder<SENDER> context) {
        boolean isNotEmpty = LiteCommandsUtil.checkAliases(shortAnnotation.value());
        if (!isNotEmpty) {
            throw new IllegalArgumentException("Route name cannot be empty");
        }

        String childName = executeAnnotation.name();
        if (childName.isEmpty()) {
            throw new IllegalArgumentException("@ShortCommand annotation cannot be declared on root executor");
        }

        context.getRealRoute().getChild(childName)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find route with name " + childName))
            .shortRoutes(Arrays.asList(shortAnnotation.value()));

        return context;
    }


}
