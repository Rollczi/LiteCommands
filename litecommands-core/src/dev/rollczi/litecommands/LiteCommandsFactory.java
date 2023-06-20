package dev.rollczi.litecommands;

import dev.rollczi.litecommands.builder.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissionValidator;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.wrapper.implementations.CompletableFutureWrapper;
import dev.rollczi.litecommands.wrapper.implementations.OptionWrapper;
import dev.rollczi.litecommands.wrapper.implementations.OptionalWrapper;
import dev.rollczi.litecommands.argument.resolver.baisc.NumberArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.baisc.StringArgumentResolver;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformSender;
import panda.std.Result;

public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER, C extends PlatformSettings, B extends LiteCommandsBaseBuilder<SENDER, C, B>> LiteCommandsBuilder<SENDER, C, B> builder(Class<SENDER> senderClass, Platform<SENDER, C> platform) {
        return new LiteCommandsBaseBuilder<SENDER, C, B>(senderClass, platform)
            .result(Throwable.class, (invocation, result) -> result.printStackTrace())

            .wrapper(new OptionWrapper())
            .wrapper(new OptionalWrapper())
            .wrapper(new CompletableFutureWrapper())

            .context(senderClass, invocation -> ContextResult.ok(() -> invocation.sender()))
            .context(String[].class, invocation -> ContextResult.ok(() -> invocation.arguments().asArray()))
            .context(PlatformSender.class, invocation -> ContextResult.ok(() -> invocation.platformSender()))
            .context(Invocation.class, invocation -> ContextResult.ok(() -> invocation)) // Do not use short method reference here (it will cause bad return type in method reference on Java 8)

            .validator(Scope.global(), new MissingPermissionValidator<>())

//            .resultMapper(MissingPermissions.class, new GuideMissingPermission<>()) TODO: Fix this

            .argument(String.class, new StringArgumentResolver<>())
            .argument(Long.class, NumberArgumentResolver.ofLong())
            .argument(long.class, NumberArgumentResolver.ofLong())
            .argument(Integer.class, NumberArgumentResolver.ofInteger())
            .argument(int.class, NumberArgumentResolver.ofInteger())
            .argument(Double.class, NumberArgumentResolver.ofDouble())
            .argument(double.class, NumberArgumentResolver.ofDouble())
            .argument(Float.class, NumberArgumentResolver.ofFloat())
            .argument(float.class, NumberArgumentResolver.ofFloat())
            .argument(Byte.class, NumberArgumentResolver.ofByte())
            .argument(byte.class, NumberArgumentResolver.ofByte())
            .argument(Short.class, NumberArgumentResolver.ofShort())
            .argument(short.class, NumberArgumentResolver.ofShort())

            ;
    }

}
