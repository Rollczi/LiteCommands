package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.annotation.LiteCommandsAnnotationBuilder;
import dev.rollczi.litecommands.modern.annotation.LiteCommandsAnnotationBuilderImpl;
import dev.rollczi.litecommands.modern.annotation.command.MethodCommandExecutorFactory;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.annotation.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.modern.annotation.route.RootRoute;
import dev.rollczi.litecommands.modern.annotation.route.Route;
import dev.rollczi.litecommands.modern.guide.GuideMissingPermissionHandler;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.permission.MissingPermissionValidator;
import dev.rollczi.litecommands.modern.permission.MissingPermissions;
import dev.rollczi.litecommands.modern.permission.annotation.Permission;
import dev.rollczi.litecommands.modern.permission.annotation.PermissionExcluded;
import dev.rollczi.litecommands.modern.permission.annotation.Permissions;
import dev.rollczi.litecommands.modern.permission.annotation.PermissionsExcluded;
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

             .contextualBind(senderClass, Invocation::getSender)

             .contextualBind(String[].class, Invocation::arguments)
             .contextualBind(PlatformSender.class, Invocation::getPlatformSender)
             .contextualBind(Invocation.class, invocation -> invocation)

             .validator(new MissingPermissionValidator<>())

             .argument(String.class, new StringArgumentResolver<>());
    }

    @SuppressWarnings("unchecked")
    public static <SENDER, B extends LiteCommandsAnnotationBuilder<SENDER, B>> LiteCommandsAnnotationBuilder<SENDER, B> annotation(Class<SENDER> senderClass) {
        LiteCommandsInternalPattern<SENDER> internalPattern = (LiteCommandsInternalPattern<SENDER>) LiteCommandsFactory.base(senderClass);
        MethodCommandExecutorFactory<SENDER> executorFactory = MethodCommandExecutorFactory.create(internalPattern.getWrappedExpectedContextualService(), internalPattern.getArgumentService().getResolverRegistry());

        return (B) new LiteCommandsAnnotationBuilderImpl<>(internalPattern)
            // class
            .annotation(Route.class, new Route.AnnotationResolver<>())
            .annotation(RootRoute.class, new RootRoute.AnnotationResolver<>())
            // method
            .annotation(Execute.class, new ExecuteAnnotationResolver<>(executorFactory))
            // meta of class and method
            .annotation(Permission.class, new Permission.AnnotationResolver<>())
            .annotation(Permissions.class, new Permissions.AnnotationResolver<>())
            .annotation(PermissionExcluded.class, new PermissionExcluded.AnnotationResolver<>())
            .annotation(PermissionsExcluded.class, new PermissionsExcluded.AnnotationResolver<>())

            // guide
            .resultHandler(MissingPermissions.class, new GuideMissingPermissionHandler<>())
            ;
    }



}
