package dev.rollczi.litecommands.execute;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.util.LiteCommandsUtil;

import java.util.Arrays;

public class ExecuteAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onExecutorStructure(Execute.class, (annotation, builder, executorBuilder) -> resolve(annotation, builder, executorBuilder));
    }

    private CommandBuilder<SENDER> resolve(Execute annotation, CommandBuilder<SENDER> context, CommandExecutorProvider<SENDER> executorProvider) {
        boolean isNotEmpty = LiteCommandsUtil.checkConsistent(annotation.name(), annotation.aliases());

        if (isNotEmpty) {
            context.getRealRoute().appendChild(CommandBuilder.<SENDER>create()
                .routeName(annotation.name())
                .routeAliases(Arrays.asList(annotation.aliases()))
                .applyOnRoute(builder -> builder.appendExecutor(executorProvider)));

            return context;
        }

        context.getRealRoute().appendExecutor(executorProvider);
        return context;
    }

}
