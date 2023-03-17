package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.guide.GuideMissingPermissionHandler;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.permission.MissingPermissionValidator;
import dev.rollczi.litecommands.modern.permission.MissingPermissions;
import dev.rollczi.litecommands.modern.platform.PlatformSender;
import dev.rollczi.litecommands.modern.wrapper.implementations.CompletableFutureWrappedExpectedFactory;
import dev.rollczi.litecommands.modern.wrapper.implementations.OptionWrappedExpectedFactory;
import dev.rollczi.litecommands.modern.argument.type.baisc.StringArgumentResolver;
import panda.std.Result;

public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER, C extends LiteConfiguration, B extends LiteCommandsBaseBuilder<SENDER, C, B>> LiteCommandsBuilder<SENDER, C, B> builder(Class<SENDER> senderClass, C configuration) {
         return new LiteCommandsBaseBuilder<SENDER, C, B>(senderClass, configuration)
             .resultHandler(Throwable.class, (invocation, result) -> result.printStackTrace())

             .wrapperFactory(new OptionWrappedExpectedFactory())
             .wrapperFactory(new CompletableFutureWrappedExpectedFactory())

             .contextualBind(senderClass, invocation -> Result.ok(invocation.getSender()))

             .contextualBind(String[].class, invocation -> Result.ok(invocation.arguments()))
             .contextualBind(PlatformSender.class, invocation -> Result.ok(invocation.getPlatformSender()))
             .contextualBind(Invocation.class, Result::ok)

             .validator(new MissingPermissionValidator<>())
             .resultHandler(MissingPermissions.class, new GuideMissingPermissionHandler<>())

             .argument(String.class, new StringArgumentResolver<>());
    }

}
