package dev.rollczi.litecommands.annotations.execute;

import dev.rollczi.litecommands.annotations.command.MethodCommandExecutorService;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMethodResolver;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.editor.CommandEditorExecutorBuilder;
import dev.rollczi.litecommands.util.LiteCommandsUtil;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ExecuteAnnotationResolver<SENDER> implements CommandAnnotationMethodResolver<SENDER, Execute> {

    private final MethodCommandExecutorService<SENDER> methodCommandExecutorService;

    public ExecuteAnnotationResolver(MethodCommandExecutorService<SENDER> methodCommandExecutorService) {
        this.methodCommandExecutorService = methodCommandExecutorService;
    }

    @Override
    public CommandEditorContext<SENDER> resolve(Object instance, Method method, Execute annotation, CommandEditorContext<SENDER> context, CommandEditorExecutorBuilder<SENDER> executorBuilder) {
        boolean isNotEmpty = LiteCommandsUtil.checkConsistent(annotation.route(), annotation.aliases());

        CommandExecutor<SENDER> executor = this.methodCommandExecutorService.create(instance, method);

        executorBuilder.setExecutor(executor);

        if (isNotEmpty) {
            context.route().appendChild(CommandEditorContext.<SENDER>create()
                .routeName(annotation.route())
                .routeAliases(Arrays.asList(annotation.aliases()))
                .appendExecutor(executorBuilder));

            return context;
        }

        context.route().appendExecutor(executorBuilder);
        return context;
    }

}
