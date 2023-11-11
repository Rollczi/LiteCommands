package dev.rollczi.litecommands.annotations.execute;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.util.LiteCommandsUtil;
import java.util.Arrays;

public class ShortCommandAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onExecutorStructure(
            Execute.class,
            (executeAnnotation, builder, executorBuilder) -> invoker.onExecutorStructure(
                ShortCommand.class,
                (shortAnnotation, shortBuilder, shortExecutorBuilder) -> resolve(executeAnnotation, shortAnnotation, builder)
            ).getResult()
        );
    }

    private CommandBuilder<SENDER> resolve(Execute executeAnnotation, ShortCommand shortAnnotation, CommandBuilder<SENDER> context) {
        boolean isNotEmpty = LiteCommandsUtil.checkConsistent(shortAnnotation.name(), shortAnnotation.aliases());
        if (!isNotEmpty) {
            throw new IllegalArgumentException("Route name cannot be empty");
        }

        String childName = executeAnnotation.name();
        if (childName.isEmpty()) {
            throw new IllegalArgumentException("@ShortCommand annotation cannot be declared on root executor");
        }

        CommandBuilder<SENDER> child = context.getRealRoute().getChild(childName)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find route with name " + childName));

        child.shortRouteName(shortAnnotation.name());
        child.shortRouteAliases(Arrays.asList(shortAnnotation.aliases()));

        return context;
    }


}
