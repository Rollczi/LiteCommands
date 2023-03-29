package dev.rollczi.litecommands;

import dev.rollczi.litecommands.builder.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.guide.GuideMissingPermission;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissionValidator;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.wrapper.implementations.CompletableFutureWrappedExpectedFactory;
import dev.rollczi.litecommands.wrapper.implementations.OptionWrappedExpectedFactory;
import dev.rollczi.litecommands.wrapper.implementations.OptionalWrappedExpectedFactory;
import dev.rollczi.litecommands.argument.type.baisc.AbstractNumberArgumentResolver;
import dev.rollczi.litecommands.argument.type.baisc.StringArgumentResolver;
import dev.rollczi.litecommands.platform.LiteSettings;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformSender;
import panda.std.Result;

public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER, C extends LiteSettings, B extends LiteCommandsBaseBuilder<SENDER, C, B>> LiteCommandsBuilder<SENDER, C, B> builder(Class<SENDER> senderClass, Platform<SENDER, C> platform) {
        return new LiteCommandsBaseBuilder<SENDER, C, B>(senderClass, platform)
            .resultHandler(Throwable.class, (invocation, result) -> result.printStackTrace())

            .registerWrapperFactory(new OptionWrappedExpectedFactory())
            .registerWrapperFactory(new OptionalWrappedExpectedFactory())
            .registerWrapperFactory(new CompletableFutureWrappedExpectedFactory())

            .contextualBind(senderClass, invocation -> Result.ok(invocation.getSender()))

            .contextualBind(String[].class, invocation -> Result.ok(invocation.arguments()))
            .contextualBind(PlatformSender.class, invocation -> Result.ok(invocation.getPlatformSender()))
            .contextualBind(Invocation.class, invocation -> Result.ok(invocation)) // Do not use short method reference here (it will cause bad return type in method reference on Java 8)

            .globalValidator(new MissingPermissionValidator<>())
            .resultMapper(MissingPermissions.class, new GuideMissingPermission<>())

            .argument(String.class, new StringArgumentResolver<>())

            .argument(Long.class, AbstractNumberArgumentResolver.ofLong())
            ;
    }

}
