package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.argument.block.Block;
import dev.rollczi.litecommands.argument.block.BlockArgument;
import dev.rollczi.litecommands.argument.flag.Flag;
import dev.rollczi.litecommands.argument.flag.FlagArgument;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.argument.joiner.JoinerArgument;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.amount.Between;
import dev.rollczi.litecommands.command.amount.Max;
import dev.rollczi.litecommands.command.amount.Min;
import dev.rollczi.litecommands.command.amount.Required;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.ExecutedPermission;
import dev.rollczi.litecommands.command.permission.ExecutedPermissions;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.permission.Permissions;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.platform.LiteSender;
import panda.std.Blank;
import panda.std.Option;
import panda.std.Result;

import java.util.function.Supplier;

import static dev.rollczi.litecommands.argument.simple.OneArgument.create;
import static dev.rollczi.litecommands.command.sugesstion.Suggestion.of;
import static panda.std.Blank.BLANK;

public final class LiteFactory {

    private static final OneArgument<Boolean> BOOLEAN_ARG = create((invocation, argument) -> Option.of(argument)
                    .filter(arg -> arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false"))
                    .map(Boolean::parseBoolean)
                    .toResult(BLANK),
            invocation -> of("true", "false")
    );

    private static final OneArgument<Character> CHARACTER_ARG = create((inv, argument) -> Option.of(argument)
                    .filter(arg -> arg.length() == 1)
                    .map(arg -> arg.charAt(0))
                    .toResult(BLANK),
            invocation -> of("abcdefghijklmnoprstuwxyz0123456789".split(""))
    );

    private static final OneArgument<Long> LONG_ARG =     create((inv, arg) -> parse(() -> Long.parseLong(arg)), inv -> of("0", "1", "5", "10", "50", "100", "500"));
    private static final OneArgument<Integer> INT_ARG =   create((inv, arg) -> parse(() -> Integer.parseInt(arg)), inv -> of("0", "1", "5", "10", "50", "100", "500"));
    private static final OneArgument<Short> SHORT_ARG =   create((inv, arg) -> parse(() -> Short.parseShort(arg)), inv -> of("0", "1", "5", "10", "50"));
    private static final OneArgument<Byte> BYTE_ARG =     create((inv, arg) -> parse(() -> Byte.parseByte(arg)), inv -> of("0", "1", "5", "10", "50"));
    private static final OneArgument<Double> DOUBLE_ARG = create((inv, arg) -> parse(() -> Double.parseDouble(arg)), inv -> of("0", "1", "1.5", "10", "10.5", "100", "100.5"));
    private static final OneArgument<Float> FLOAT_ARG =   create((inv, arg) -> parse(() -> Float.parseFloat(arg)), inv -> of("0", "1", "1.5", "10", "10.5", "100", "100.5"));

    private LiteFactory() {
    }

    public static <SENDER> LiteCommandsBuilder<SENDER> builder(Class<SENDER> senderType) {
        return LiteCommandsBuilderImpl.<SENDER>builder(senderType)
                .configureFactory(factory -> {
                    factory.annotationResolver(Section.RESOLVER);
                    factory.annotationResolver(Execute.RESOLVER);
                    factory.annotationResolver(Permission.RESOLVER);
                    factory.annotationResolver(Permissions.RESOLVER);
                    factory.annotationResolver(ExecutedPermission.EXECUTED_PERMISSION_RESOLVER);
                    //factory.annotationResolver(ExecutedPermissions.EXECUTED_PERMISSIONS_RESOLVER);
                    factory.annotationResolver(Min.RESOLVER);
                    factory.annotationResolver(Max.RESOLVER);
                    factory.annotationResolver(Required.RESOLVER);
                    factory.annotationResolver(Between.RESOLVER);
                })
                .argument(Flag.class, boolean.class, new FlagArgument<>())
                .argument(Flag.class, Boolean.class, new FlagArgument<>())
                .argument(Joiner.class, String.class, new JoinerArgument<>())
                .argument(Block.class, Object.class, new BlockArgument<>())

                .argument(String.class, (invocation, argument) -> Result.ok(argument))
                .argument(boolean.class, BOOLEAN_ARG)
                .argument(Boolean.class, BOOLEAN_ARG)
                .argument(long.class, LONG_ARG)
                .argument(Long.class, LONG_ARG)
                .argument(int.class, INT_ARG)
                .argument(Integer.class, INT_ARG)
                .argument(short.class, SHORT_ARG)
                .argument(Short.class, SHORT_ARG)
                .argument(byte.class, BYTE_ARG)
                .argument(Byte.class, BYTE_ARG)
                .argument(double.class, DOUBLE_ARG)
                .argument(Double.class, DOUBLE_ARG)
                .argument(float.class, FLOAT_ARG)
                .argument(Float.class, FLOAT_ARG)
                .argument(char.class, CHARACTER_ARG)
                .argument(Character.class, CHARACTER_ARG)

                .contextualBind(LiteInvocation.class, (sender, invocation) -> Result.ok(invocation.toLite()))
                .contextualBind(LiteSender.class, (sender, invocation) -> Result.ok(invocation.sender()))
                .contextualBind(String[].class, (sender, invocation) -> Result.ok(invocation.arguments()))
                .contextualBind(senderType, (sender, invocation) -> Result.ok(sender));
    }

    private static <T> Result<T, Blank> parse(Supplier<T> parse) {
        return Result.attempt(NumberFormatException.class, parse::get).mapErrToBlank();
    }

}
