package dev.rollczi.litecommands.annotations.fastuse;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.util.LiteCommandsUtil;
import java.util.Arrays;

public class FastUseAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onExecutorStructure(FastUse.class, (annotation, builder, executorBuilder) -> resolve(annotation, builder, executorBuilder));
    }

    private CommandBuilder<SENDER> resolve(FastUse annotation, CommandBuilder<SENDER> context, CommandExecutorProvider<SENDER> executorProvider) {
        CommandBuilder<SENDER> builder = CommandBuilder.<SENDER>create()
            .routeName(annotation.name())
            .aliases(Arrays.asList(annotation.aliases()))
            .appendExecutor(executorProvider);

        if (annotation.copyMeta()) {
            builder.applyMeta(meta -> meta.apply(context.meta().copyToFastUse()));
        }

        return builder;
    }

}
