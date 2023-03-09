package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.guide.GuideMissingPermissionHandler;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.permission.MissingPermissionValidator;
import dev.rollczi.litecommands.modern.permission.MissingPermissions;
import dev.rollczi.litecommands.modern.platform.PlatformSender;
import dev.rollczi.litecommands.modern.wrapper.implementations.CompletableFutureWrappedExpectedFactory;
import dev.rollczi.litecommands.modern.wrapper.implementations.OptionWrappedExpectedFactory;
import dev.rollczi.litecommands.modern.argument.type.baisc.StringArgumentResolver;

public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER, B extends LiteCommandsBaseBuilder<SENDER, B>> LiteCommandsBuilder<SENDER, B> builder(Class<SENDER> senderClass) {
         return new LiteCommandsBaseBuilder<SENDER, B>(senderClass)
             .resultHandler(Throwable.class, (invocation, result) -> result.printStackTrace())

             .wrapperFactory(new OptionWrappedExpectedFactory())
             .wrapperFactory(new CompletableFutureWrappedExpectedFactory())

             .contextualBind(senderClass, Invocation::getSender)

             .contextualBind(String[].class, Invocation::arguments)
             .contextualBind(PlatformSender.class, Invocation::getPlatformSender)
             .contextualBind(Invocation.class, invocation -> invocation)

             .validator(new MissingPermissionValidator<>())
             .resultHandler(MissingPermissions.class, new GuideMissingPermissionHandler<>())

             .argument(String.class, new StringArgumentResolver<>());
    }

}
