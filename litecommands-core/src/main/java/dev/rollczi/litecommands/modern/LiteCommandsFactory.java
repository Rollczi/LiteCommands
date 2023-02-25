package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.annotation.LiteCommandsAnnotationBuilder;
import dev.rollczi.litecommands.modern.annotation.LiteCommandsAnnotationBuilderImpl;
import dev.rollczi.litecommands.modern.annotation.argument.Arg;
import dev.rollczi.litecommands.modern.annotation.command.MethodCommandExecutorFactory;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.annotation.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.modern.annotation.route.Route;
import dev.rollczi.litecommands.modern.annotation.route.RouteAnnotationResolver;
import dev.rollczi.litecommands.modern.contextual.warpped.implementations.CompletableFutureWrappedExpectedContextualFactory;
import dev.rollczi.litecommands.modern.contextual.warpped.implementations.OptionWrappedExpectedContextualFactory;
import dev.rollczi.litecommands.modern.contextual.warpped.implementations.ValueWrappedExpectedContextualFactory;
import dev.rollczi.litecommands.modern.core.LiteCommandsCoreBuilder;
import dev.rollczi.litecommands.modern.core.argument.StringArgument;

public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER, B extends LiteCommandsCoreBuilder<SENDER, B>> LiteCommandsCoreBuilder<SENDER, B> base(Class<SENDER> senderClass) {
        return new LiteCommandsCoreBuilder<SENDER, B>(senderClass)
            .wrappedExpectedContextualFactory(new OptionWrappedExpectedContextualFactory())
            .wrappedExpectedContextualFactory(new CompletableFutureWrappedExpectedContextualFactory())
            .argument(String.class, new StringArgument<>());
    }

    @SuppressWarnings("unchecked")
    public static <SENDER, B extends LiteCommandsAnnotationBuilder<SENDER, B>> LiteCommandsAnnotationBuilder<SENDER, B> annotation(Class<SENDER> senderClass) {
        LiteCommandsInternalPattern<SENDER> internalPattern = LiteCommandsFactory.base(senderClass);
        MethodCommandExecutorFactory<SENDER> executorFactory = MethodCommandExecutorFactory.create(internalPattern.getWrappedExpectedContextualService(), internalPattern.getArgumentService().getResolverRegistry());

        return (B) new LiteCommandsAnnotationBuilderImpl<>(internalPattern)
            .annotation(Route.class, new RouteAnnotationResolver<>())
            .annotationMethod(Execute.class, new ExecuteAnnotationResolver<>(executorFactory));
    }

}
