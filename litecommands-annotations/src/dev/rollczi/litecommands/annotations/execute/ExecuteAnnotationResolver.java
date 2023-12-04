package dev.rollczi.litecommands.annotations.execute;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.util.LiteCommandsUtil;

import java.util.Arrays;

public class ExecuteAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onExecutorStructure(Execute.class, (annotation, context, executorProvider) -> {
            boolean isNotEmpty = LiteCommandsUtil.checkConsistent(annotation.name(), annotation.aliases());

            if (isNotEmpty) {
                context.getRealRoute().appendChild(CommandBuilder.<SENDER>create()
                    .routeName(annotation.name())
                    .routeAliases(Arrays.asList(annotation.aliases()))
                    .applyOnRoute(builder -> builder.appendExecutor(executorProvider)));

                return;
            }

            context.getRealRoute().appendExecutor(executorProvider);
        });
    }

}
