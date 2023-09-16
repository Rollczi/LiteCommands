package dev.rollczi.litecommands;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.resolver.std.NumberArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.std.StringArgumentResolver;
import dev.rollczi.litecommands.builder.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissionResultHandler;
import dev.rollczi.litecommands.permission.MissingPermissionValidator;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.ArgArgumentFactory;
import dev.rollczi.litecommands.async.AsyncAnnotationResolver;
import dev.rollczi.litecommands.command.CommandAnnotationProcessor;
import dev.rollczi.litecommands.command.RootCommandAnnotationProcessor;
import dev.rollczi.litecommands.description.DescriptionAnnotationResolver;
import dev.rollczi.litecommands.command.executor.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.flag.Flag;
import dev.rollczi.litecommands.flag.FlagArgumentFactory;
import dev.rollczi.litecommands.flag.FlagArgumentResolver;
import dev.rollczi.litecommands.join.Join;
import dev.rollczi.litecommands.join.JoinArgumentFactory;
import dev.rollczi.litecommands.join.JoinArgumentResolver;
import dev.rollczi.litecommands.meta.MarkMeta;
import dev.rollczi.litecommands.permission.Permission;
import dev.rollczi.litecommands.permission.Permissions;
import dev.rollczi.litecommands.validator.Validate;
import dev.rollczi.litecommands.context.Context;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import dev.rollczi.litecommands.wrapper.std.CompletableFutureWrapper;
import dev.rollczi.litecommands.wrapper.std.OptionWrapper;
import dev.rollczi.litecommands.wrapper.std.OptionalWrapper;

public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER, C extends PlatformSettings, B extends LiteCommandsBaseBuilder<SENDER, C, B>> LiteCommandsBuilder<SENDER, C, B> builder(Class<SENDER> senderClass, Platform<SENDER, C> platform) {
        return new LiteCommandsBaseBuilder<SENDER, C, B>(senderClass, platform).selfProcessor((builder, pattern) -> {
            WrapperRegistry wrapperRegistry = pattern.getWrapperRegistry();
            ParserRegistry<SENDER> parserRegistry = pattern.getParserRegistry();
            ContextRegistry<SENDER> contextRegistry = pattern.getContextRegistry();
            Scheduler scheduler = pattern.getScheduler();

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

                .argument(String.class, Join.ARGUMENT_KEY, new JoinArgumentResolver<>())
                .argument(boolean.class, Flag.ARGUMENT_KEY, new FlagArgumentResolver<>())
                .argument(Boolean.class, Flag.ARGUMENT_KEY, new FlagArgumentResolver<>())

                .annotationProcessor(new CommandAnnotationProcessor<>())
                .annotationProcessor(new RootCommandAnnotationProcessor<>())
                .annotationProcessor(new MarkMeta.AnnotationResolver<>())
                .annotationProcessor(new DescriptionAnnotationResolver<>())
                .annotationProcessor(new AsyncAnnotationResolver<>())
                .annotationProcessor(new Permission.AnnotationResolver<>())
                .annotationProcessor(new Permissions.AnnotationResolver<>())
                .annotationProcessor(new Validate.AnnotationResolver<>())
                .annotationProcessor(new ExecuteAnnotationResolver<>())
                .annotationProcessor(new RequirementProcessor<>(wrapperRegistry, parserRegistry, Flag.class, new FlagArgumentFactory()))
                .annotationProcessor(new RequirementProcessor<>(wrapperRegistry, parserRegistry, Arg.class, new ArgArgumentFactory()))
                .annotationProcessor(new RequirementProcessor<>(wrapperRegistry, parserRegistry, Join.class, new JoinArgumentFactory()))
                .annotationProcessor(new RequirementProcessor<>(contextRegistry, wrapperRegistry, Context.class))

                .wrapper(new OptionWrapper())
                .wrapper(new OptionalWrapper())
                .wrapper(new CompletableFutureWrapper(scheduler))

                .result(Throwable.class, (invocation, result, chain) -> result.printStackTrace())
                .result(MissingPermissions.class, new MissingPermissionResultHandler<>(pattern.getMessageRegistry()))
                ;
        });
    }

}
