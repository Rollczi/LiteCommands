package dev.rollczi.litecommands.annotations.shortcut;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.util.LiteCommandsUtil;
import java.util.Arrays;

public class ShortcutCommandAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onMethod(Execute.class,
            (method, executeAnnotation, builder, executorBuilder) -> invoker.onMethod(
                Shortcut.class,
                (sameMethod, shortAnnotation, shortBuilder, shortExecutorBuilder) -> resolve(executeAnnotation, shortAnnotation, builder, shortExecutorBuilder)
            )
        );
    }

    private CommandBuilder<SENDER> resolve(Execute executeAnnotation, Shortcut shortAnnotation, CommandBuilder<SENDER> context, CommandExecutorProvider<SENDER> executorProvider) {
        boolean isNotEmpty = LiteCommandsUtil.checkAliases(shortAnnotation.value());
        if (!isNotEmpty) {
            throw new IllegalArgumentException("Route name cannot be empty");
        }

        String childName = executeAnnotation.name();
        if (childName.isEmpty()) {
            context.getRealRoute()
                .shortcuts(executorProvider, Arrays.asList(shortAnnotation.value()));

            return context;
        }

        context.getRealRoute().getChild(childName)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find route with name " + childName))
            .shortcuts(executorProvider, Arrays.asList(shortAnnotation.value()));

        return context;
    }


}
