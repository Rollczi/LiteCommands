package dev.rollczi.litecommands;

import dev.rollczi.litecommands.builder.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.guide.GuideMissingPermission;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissionValidator;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.validator.ValidatorScope;
import dev.rollczi.litecommands.wrapper.implementations.CompletableFutureWrapper;
import dev.rollczi.litecommands.wrapper.implementations.OptionWrapper;
import dev.rollczi.litecommands.wrapper.implementations.OptionalWrapper;
import dev.rollczi.litecommands.argument.resolver.baisc.NumberArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.baisc.StringArgumentResolver;
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

            .registerWrapperFactory(new OptionWrapper())
            .registerWrapperFactory(new OptionalWrapper())
            .registerWrapperFactory(new CompletableFutureWrapper())

            .bindContext(senderClass, invocation -> Result.ok(invocation.sender()))

            .bindContext(String[].class, invocation -> Result.ok(invocation.arguments().asArray()))
            .bindContext(PlatformSender.class, invocation -> Result.ok(invocation.platformSender()))
            .bindContext(Invocation.class, invocation -> Result.ok(invocation)) // Do not use short method reference here (it will cause bad return type in method reference on Java 8)

            .withValidator(new MissingPermissionValidator<>(), ValidatorScope.GLOBAL)
            .resultMapper(MissingPermissions.class, new GuideMissingPermission<>())

            .argumentParser(String.class, new StringArgumentResolver<>())

            .argumentParser(Long.class, NumberArgumentResolver.ofLong())
            ;
    }

}
