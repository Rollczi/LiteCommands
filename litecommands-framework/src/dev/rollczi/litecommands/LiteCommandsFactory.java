package dev.rollczi.litecommands;

import dev.rollczi.litecommands.argument.resolver.standard.DurationArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.EnumArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.InstantArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.PeriodArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.StringArgumentResolver;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.flag.FlagArgument;
import dev.rollczi.litecommands.handler.exception.standard.InvocationTargetExceptionHandler;
import dev.rollczi.litecommands.handler.exception.standard.LiteCommandsExceptionHandler;
import dev.rollczi.litecommands.handler.result.standard.CompletionStageHandler;
import dev.rollczi.litecommands.handler.result.standard.OptionHandler;
import dev.rollczi.litecommands.handler.result.standard.OptionalHandler;
import dev.rollczi.litecommands.handler.exception.standard.ThrowableHandler;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageException;
import dev.rollczi.litecommands.invalidusage.InvalidUsageExceptionHandler;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandlerImpl;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.join.JoinArgument;
import dev.rollczi.litecommands.join.JoinStringArgumentResolver;
import dev.rollczi.litecommands.permission.MissingPermissionResultHandler;
import dev.rollczi.litecommands.permission.MissingPermissionValidator;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.flag.FlagArgumentResolver;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.wrapper.std.CompletableFutureWrapper;
import dev.rollczi.litecommands.wrapper.std.OptionWrapper;
import dev.rollczi.litecommands.wrapper.std.OptionalWrapper;
import panda.std.Option;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER, C extends PlatformSettings, B extends LiteCommandsBaseBuilder<SENDER, C, B>> LiteCommandsBuilder<SENDER, C, B> builder(Class<SENDER> senderClass, Platform<SENDER, C> platform) {
        return new LiteCommandsBaseBuilder<SENDER, C, B>(senderClass, platform).selfProcessor((builder, internal) -> {
            Scheduler scheduler = internal.getScheduler();

            builder
                .context(senderClass, invocation -> ContextResult.ok(() -> invocation.sender()))
                .context(String[].class, invocation -> ContextResult.ok(() -> invocation.arguments().asArray()))
                .context(PlatformSender.class, invocation -> ContextResult.ok(() -> invocation.platformSender()))
                .context(Invocation.class, invocation -> ContextResult.ok(() -> invocation)) // Do not use short method reference here (it will cause bad return type in method reference on Java 8)

                .validator(Scope.global(), new MissingPermissionValidator<>())

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
                .argument(Duration.class, new DurationArgumentResolver<>())
                .argument(Period.class, new PeriodArgumentResolver<>())
                .argument(Enum.class, new EnumArgumentResolver<>())
                .argument(Instant.class, new InstantArgumentResolver<>(internal.getMessageRegistry()))

                .argumentParser(String.class, JoinArgument.KEY, new JoinStringArgumentResolver<>())
                .argument(boolean.class, FlagArgument.KEY, new FlagArgumentResolver<>())
                .argument(Boolean.class, FlagArgument.KEY, new FlagArgumentResolver<>())

                .wrapper(new OptionWrapper())
                .wrapper(new OptionalWrapper())
                .wrapper(new CompletableFutureWrapper(scheduler))

                .exception(Throwable.class, new ThrowableHandler<>())
                .exception(InvalidUsageException.class, new InvalidUsageExceptionHandler<>())
                .exception(InvocationTargetException.class, new InvocationTargetExceptionHandler<>())
                .exception(LiteCommandsException.class, new LiteCommandsExceptionHandler<>())

                .result(Optional.class, new OptionalHandler<>())
                .result(Option.class, new OptionHandler<>())
                .result(CompletionStage.class, new CompletionStageHandler<>())
                .result(MissingPermissions.class, new MissingPermissionResultHandler<>(internal.getMessageRegistry()))
                .result(InvalidUsage.class, new InvalidUsageHandlerImpl<>(internal.getMessageRegistry()))
                ;
        });
    }

}
