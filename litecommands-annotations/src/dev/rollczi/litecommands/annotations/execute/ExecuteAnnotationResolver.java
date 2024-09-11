package dev.rollczi.litecommands.annotations.execute;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;
import dev.rollczi.litecommands.util.LiteCommandsUtil;

import java.util.Arrays;

public class ExecuteAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker
            .onMethod(Execute.class, (method, annotation, context, executorProvider) -> {
                boolean isNotEmpty = LiteCommandsUtil.checkConsistent(annotation.name(), annotation.aliases());

                if (isNotEmpty) {
                    context.getRealRoute().appendChild(CommandBuilder.<SENDER>create()
                        .routeName(annotation.name())
                        .routeAliases(Arrays.asList(annotation.aliases()))
                        .applyOnRoute(builder -> builder.appendExecutor(executorProvider)));

                    return;
                }

                context.getRealRoute().appendExecutor(executorProvider);
            })
            .onMethod(ExecuteDefault.class, (method, annotation, context, executorProvider) -> context.getRealRoute()
                .appendExecutor(parent -> {
                    CommandExecutor<SENDER> executor = executorProvider.provide(parent);

                    if (!executor.getArguments().isEmpty()) {
                        throw new LiteCommandsReflectInvocationException(method, "Default executor cannot have any arguments!");
                    }

                    executor.meta().put(Meta.IGNORE_TOO_MANY_ARGUMENTS, true);
                    return executor;
                })
            );
    }

}
