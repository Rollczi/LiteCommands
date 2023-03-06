package dev.rollczi.litecommands.modern.annotation.execute;

import dev.rollczi.litecommands.modern.annotation.command.MethodCommandExecutorFactory;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMethodResolver;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.editor.CommandEditorExecutorBuilder;
import dev.rollczi.litecommands.modern.util.LiteCommandsRulesUtil;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ExecuteAnnotationResolver<SENDER> implements CommandAnnotationMethodResolver<SENDER, Execute> {

    private final MethodCommandExecutorFactory<SENDER> methodCommandExecutorFactory;

    public ExecuteAnnotationResolver(MethodCommandExecutorFactory<SENDER> methodCommandExecutorFactory) {
        this.methodCommandExecutorFactory = methodCommandExecutorFactory;
    }

    @Override
    public CommandEditorContext<SENDER> resolve(Object instance, Method method, Execute annotation, CommandEditorContext<SENDER> context, CommandEditorExecutorBuilder<SENDER> executorBuilder) {
        boolean canUse = LiteCommandsRulesUtil.checkConsistent(annotation.name(), annotation.aliases());

        CommandExecutor<SENDER> executor = this.methodCommandExecutorFactory.create(instance, method);

        executorBuilder.setExecutor(executor);

        if (canUse) {
            return context.appendChild(CommandEditorContext.<SENDER>create()
                .routeName(annotation.name())
                .routeAliases(Arrays.asList(annotation.aliases()))
                .appendExecutor(executorBuilder)
            );
        }

        return context.appendExecutor(executorBuilder);
    }

}
