package dev.rollczi.litecommands.modern.annotation.execute;

import dev.rollczi.litecommands.modern.annotation.command.MethodCommandExecutorFactory;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMethodResolver;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.util.LiteCommandsRulesUtil;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ExecuteAnnotationResolver implements CommandAnnotationMethodResolver<Execute> {

    private final MethodCommandExecutorFactory methodCommandExecutorFactory;

    public ExecuteAnnotationResolver(MethodCommandExecutorFactory methodCommandExecutorFactory) {
        this.methodCommandExecutorFactory = methodCommandExecutorFactory;
    }

    @Override
    public CommandEditorContext resolve(Object instance, Method method, Execute annotation, CommandEditorContext context) {
        boolean canUse = LiteCommandsRulesUtil.checkConsistent(annotation.name(), annotation.aliases());

        CommandExecutor executor = this.methodCommandExecutorFactory.create(instance, method);

        if (canUse) {
            return context.editChild(annotation.name(), child -> child
                .name(annotation.name())
                .aliases(Arrays.asList(annotation.aliases()))
                .appendExecutor(executor)
            );
        }

        return context.appendExecutor(executor);
    }

}
