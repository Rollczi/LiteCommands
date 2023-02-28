package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.annotation.LiteCommandsAnnotationBuilder;
import dev.rollczi.litecommands.modern.annotation.LiteCommandsAnnotationBuilderImpl;
import dev.rollczi.litecommands.modern.annotation.command.MethodCommandExecutorFactory;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.annotation.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.modern.annotation.route.Route;
import dev.rollczi.litecommands.modern.annotation.route.RouteAnnotationResolver;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.platform.PlatformSender;
import dev.rollczi.litecommands.modern.wrapper.implementations.CompletableFutureWrappedExpectedFactory;
import dev.rollczi.litecommands.modern.wrapper.implementations.OptionWrappedExpectedFactory;
import dev.rollczi.litecommands.modern.argument.type.baisc.StringArgumentResolver;

public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER, B extends LiteCommandsBaseBuilder<SENDER, B>> LiteCommandsBuilder<SENDER, B> base(Class<SENDER> senderClass) {
         return new LiteCommandsBaseBuilder<SENDER, B>(senderClass)
             .resultHandler(Throwable.class, (invocation, result) -> result.printStackTrace())

             .wrapperFactory(new OptionWrappedExpectedFactory())
             .wrapperFactory(new CompletableFutureWrappedExpectedFactory())

             .contextualBind(senderClass, Invocation::handle)

             .contextualBind(String[].class, Invocation::arguments)
             .contextualBind(PlatformSender.class, Invocation::platformSender)
             .contextualBind(Invocation.class, invocation -> invocation)

             .argument(String.class, new StringArgumentResolver<>());
    }

    @SuppressWarnings("unchecked")
    public static <SENDER, B extends LiteCommandsAnnotationBuilder<SENDER, B>> LiteCommandsAnnotationBuilder<SENDER, B> annotation(Class<SENDER> senderClass) {
        LiteCommandsInternalPattern<SENDER> internalPattern = (LiteCommandsInternalPattern<SENDER>) LiteCommandsFactory.base(senderClass);
        MethodCommandExecutorFactory<SENDER> executorFactory = MethodCommandExecutorFactory.create(internalPattern.getWrappedExpectedContextualService(), internalPattern.getArgumentService().getResolverRegistry());

        return (B) new LiteCommandsAnnotationBuilderImpl<>(internalPattern)
            .annotation(Route.class, new RouteAnnotationResolver<>())
            .annotationMethod(Execute.class, new ExecuteAnnotationResolver<>(executorFactory));
    }

}
