package dev.rollczi.litecommands;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.profile.ProfileNamespaces;
import dev.rollczi.litecommands.argument.resolver.collector.CollectionArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.ArrayArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.LinkedHashSetArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.LinkedListArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.ArrayListArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.SetArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.TreeSetArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.StackArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.VectorArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.completeable.CompletableFutureResolver;
import dev.rollczi.litecommands.argument.resolver.nullable.NullableArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.optional.OptionalArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.BigDecimalArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.BigIntegerArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.BooleanArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.DurationArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.EnumArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.InstantArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.LocalDateTimeArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.PeriodArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.StringArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.UUIDArgumentResolver;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.cooldown.CooldownState;
import dev.rollczi.litecommands.cooldown.CooldownStateResultHandler;
import dev.rollczi.litecommands.cooldown.CooldownStateController;
import dev.rollczi.litecommands.event.EventPublisher;
import dev.rollczi.litecommands.handler.exception.standard.InvocationTargetExceptionHandler;
import dev.rollczi.litecommands.handler.exception.standard.LiteCommandsExceptionHandler;
import dev.rollczi.litecommands.handler.result.standard.ArrayHandler;
import dev.rollczi.litecommands.handler.result.standard.CollectionHandler;
import dev.rollczi.litecommands.handler.result.standard.CompletionStageHandler;
import dev.rollczi.litecommands.handler.exception.standard.ThrowableHandler;
import dev.rollczi.litecommands.handler.result.standard.OptionalHandler;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageException;
import dev.rollczi.litecommands.invalidusage.InvalidUsageExceptionHandler;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandlerImpl;
import dev.rollczi.litecommands.invalidusage.InvalidUsageResultController;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.join.JoinStringArgumentResolver;
import dev.rollczi.litecommands.literal.LiteralArgumentResolver;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.permission.MissingPermissionResultHandler;
import dev.rollczi.litecommands.permission.PermissionValidator;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.PermissionValidationServiceImpl;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.flag.FlagArgumentResolver;
import dev.rollczi.litecommands.quoted.QuotedStringArgumentResolver;
import static dev.rollczi.litecommands.reflect.type.TypeRange.downwards;
import static dev.rollczi.litecommands.reflect.type.TypeRange.upwards;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.validator.ValidatorExecutionController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@SuppressWarnings("Convert2MethodRef")
public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER, C extends PlatformSettings, B extends LiteCommandsBaseBuilder<SENDER, C, B>> LiteCommandsBuilder<SENDER, C, B> builder(Class<SENDER> senderClass, Platform<SENDER, C> platform) {
        return new LiteCommandsBaseBuilder<SENDER, C, B>(senderClass, platform).self((builder, internal) -> {
            Scheduler scheduler = internal.getScheduler();
            MessageRegistry<SENDER> messageRegistry = internal.getMessageRegistry();
            ParserRegistry<SENDER> parser = internal.getParserRegistry();
            SuggesterRegistry<SENDER> suggester = internal.getSuggesterRegistry();
            EventPublisher eventPublisher = internal.getEventPublisher();

            List<Class<?>> excluded = Arrays.asList(Cloneable.class, Serializable.class, Object.class);

            builder.advanced()

                .context(senderClass, invocation -> ContextResult.ok(() -> invocation.sender()))
                .context(String[].class, invocation -> ContextResult.ok(() -> invocation.arguments().asArray()))
                .context(PlatformSender.class, invocation -> ContextResult.ok(() -> invocation.platformSender()))
                .context(Invocation.class, invocation -> ContextResult.ok(() -> invocation)) // Do not use short method reference here (it will cause bad return type in method reference on Java 8)

                .validator(Scope.global(), new PermissionValidator<>(new PermissionValidationServiceImpl(eventPublisher)))

                .listener(new ValidatorExecutionController<>(internal.getValidatorService()))
                .listener(new CooldownStateController<>(internal.getCooldownService()))
                .listener(new InvalidUsageResultController<>(internal.getSchematicGenerator()))

                .argument(String.class, new StringArgumentResolver<>())
                .argument(Boolean.class, new BooleanArgumentResolver<>())
                .argument(boolean.class, new BooleanArgumentResolver<>())
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
                .argument(BigInteger.class, new BigIntegerArgumentResolver<>(messageRegistry))
                .argument(BigDecimal.class, new BigDecimalArgumentResolver<>(messageRegistry))
                .argument(Instant.class, new InstantArgumentResolver<>(messageRegistry))
                .argument(LocalDateTime.class, new LocalDateTimeArgumentResolver<>(messageRegistry))
                .argument(UUID.class, new UUIDArgumentResolver<>(messageRegistry))

                .argument(upwards(Enum.class), new EnumArgumentResolver<>())
                .argument(Optional.class, new OptionalArgumentResolver<>(parser))
                .argument(CompletableFuture.class, new CompletableFutureResolver<>(scheduler, parser))
                .argument(upwards(Object.class), ProfileNamespaces.NULLABLE, new NullableArgumentResolver<>(parser, suggester))

                .argument(String.class, ProfileNamespaces.LITERAL, new LiteralArgumentResolver<>())

                .argument(String.class, ProfileNamespaces.QUOTED, new QuotedStringArgumentResolver<>(suggester))
                .argumentParser(String.class, ProfileNamespaces.JOIN, new JoinStringArgumentResolver<>())
                .argument(boolean.class, ProfileNamespaces.FLAG, new FlagArgumentResolver<>())
                .argument(Boolean.class, ProfileNamespaces.FLAG, new FlagArgumentResolver<>())

                .argument(downwards(LinkedHashSet.class, excluded), ProfileNamespaces.VARARGS, new LinkedHashSetArgumentResolver<>(parser, suggester))
                .argument(downwards(TreeSet.class, excluded), ProfileNamespaces.VARARGS, new TreeSetArgumentResolver<>(parser, suggester))
                .argument(downwards(Set.class, excluded), ProfileNamespaces.VARARGS, new SetArgumentResolver<>(parser, suggester))
                .argument(downwards(Stack.class, excluded), ProfileNamespaces.VARARGS, new StackArgumentResolver<>(parser, suggester))
                .argument(downwards(Vector.class, excluded), ProfileNamespaces.VARARGS, new VectorArgumentResolver<>(parser, suggester))
                .argument(downwards(LinkedList.class, excluded), ProfileNamespaces.VARARGS, new LinkedListArgumentResolver<>(parser, suggester))
                .argument(downwards(ArrayList.class, excluded), ProfileNamespaces.VARARGS, new ArrayListArgumentResolver<>(parser, suggester))
                .argument(downwards(Collection.class, excluded), ProfileNamespaces.VARARGS, new CollectionArgumentResolver<>(parser, suggester))

                .argument(upwards(Object.class), ProfileNamespaces.VARARGS, new ArrayArgumentResolver<>(parser, suggester))

                .exception(Throwable.class, new ThrowableHandler<>())
                .exception(InvalidUsageException.class, new InvalidUsageExceptionHandler<>())
                .exception(InvocationTargetException.class, new InvocationTargetExceptionHandler<>())
                .exception(LiteCommandsException.class, new LiteCommandsExceptionHandler<>())

                .result(Object[].class, new ArrayHandler<>())
                .result(Optional.class, new OptionalHandler<>())
                .result(CompletionStage.class, new CompletionStageHandler<>())
                .result(Collection.class, new CollectionHandler<>())
                .result(MissingPermissions.class, new MissingPermissionResultHandler<>(messageRegistry))
                .result(CooldownState.class, new CooldownStateResultHandler<>(messageRegistry))
                .result(InvalidUsage.class, new InvalidUsageHandlerImpl<>(messageRegistry))

                ;
        });
    }

}
