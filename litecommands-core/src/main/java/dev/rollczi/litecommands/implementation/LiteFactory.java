package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.block.Block;
import dev.rollczi.litecommands.argument.block.BlockArgument;
import dev.rollczi.litecommands.argument.enumeration.EnumArgument;
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
import dev.rollczi.litecommands.command.permission.LitePermissions;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.litecommands.scheme.Scheme;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Blank;
import panda.std.Option;
import panda.std.Result;

import java.util.function.Function;
import java.util.function.Supplier;

import static dev.rollczi.litecommands.suggestion.Suggestion.of;
import static panda.std.Blank.BLANK;

public final class LiteFactory {

    private static final OneArgument<Boolean> BOOLEAN_ARG = OneArgument.create((invocation, argument) -> Option.of(argument)
                    .filter(arg -> arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false"))
                    .map(Boolean::parseBoolean)
                    .toResult(BLANK),
            invocation -> of("true", "false")
    );

    private static final OneArgument<Character> CHARACTER_ARG = OneArgument.create((inv, argument) -> Option.of(argument)
                    .filter(arg -> arg.length() == 1)
                    .map(arg -> arg.charAt(0))
                    .toResult(BLANK),
            invocation -> of("abcdefghijklmnoprstuwxyz0123456789".split(""))
    );

    private static final OneArgument<Long> LONG_ARG =     create(Long::parseLong,     "0", "1", "5", "10", "50", "100", "500");
    private static final OneArgument<Integer> INT_ARG =   create(Integer::parseInt,   "0", "1", "5", "10", "50", "100", "500");
    private static final OneArgument<Short> SHORT_ARG =   create(Short::parseShort,   "0", "1", "5", "10", "50");
    private static final OneArgument<Byte> BYTE_ARG =     create(Byte::parseByte,     "0", "1", "5", "10", "50");
    private static final OneArgument<Double> DOUBLE_ARG = create(Double::parseDouble, "0", "1", "1.5", "10", "10.5", "100", "100.5");
    private static final OneArgument<Float> FLOAT_ARG =   create(Float::parseFloat,   "0", "1", "1.5", "10", "10.5", "100", "100.5");

    private LiteFactory() {
    }

    private static <T> Result<T, Blank> parse(Supplier<T> parse) {
        return Result.attempt(NumberFormatException.class, parse::get).mapErrToBlank();
    }

    public static <SENDER> LiteCommandsBuilder<SENDER> builder(Class<SENDER> senderType) {
        return LiteCommandsBuilderImpl.builder(senderType)
                .configureFactory(factory -> {
                    factory.annotationResolver(Section.RESOLVER);
                    factory.annotationResolver(Execute.RESOLVER);
                    factory.annotationResolver(Permission.RESOLVER);
                    factory.annotationResolver(Permission.REPEATABLE_RESOLVER);
                    factory.annotationResolver(ExecutedPermission.RESOLVER);
                    factory.annotationResolver(ExecutedPermission.REPEATABLE_RESOLVER);
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

                .argument(Arg.class, Enum.class, new EnumArgument<>())

                .resultHandler(boolean.class, (sender, invocation, value) -> {})
                .resultHandler(Boolean.class, (sender, invocation, value) -> {})

                .redirectResult(Scheme.class, String.class, scheme -> String.join(System.lineSeparator(), scheme.getSchemes()))
                .redirectResult(LitePermissions.class, String.class, scheme -> String.join(System.lineSeparator(), scheme.getPermissions()))

                .contextualBind(LiteInvocation.class, (sender, invocation) -> Result.ok(invocation.toLite()))
                .contextualBind(LiteSender.class, (sender, invocation) -> Result.ok(invocation.sender()))
                .contextualBind(String[].class, (sender, invocation) -> Result.ok(invocation.arguments()))
                .contextualBind(senderType, (sender, invocation) -> Result.ok(sender));
    }

    private static <T> OneArgument<T> create(Function<String, T> parse, String... suggestions) {
        return OneArgument.create((invocation, arg) -> parse(parse, arg), invocation -> Suggestion.of(suggestions), (inv, suggestion) -> validate(parse, suggestion));
    }

    private static <T> Result<T, Blank> parse(Function<String, T> parse, String value) {
        return Result.attempt(NumberFormatException.class, () -> parse.apply(value)).mapErrToBlank();
    }

    private static boolean validate(Function<String, ?> parse, Suggestion suggestion) {
        try {
            parse.apply(suggestion.single());
            return true;
        }
        catch (NumberFormatException ignore) {
            return false;
        }
    }

}
