package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Args;
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
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.handle.Redirector;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.litecommands.schematic.Schematic;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Blank;
import panda.std.Option;
import panda.std.Result;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;

import static dev.rollczi.litecommands.suggestion.Suggestion.of;
import static panda.std.Blank.BLANK;

public final class LiteFactory {

    private static final OneArgument<String>  STRING_ARG = create(arg -> arg, "text");

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

    private static final OneArgument<Long> LONG_ARG =          create(Long::parseLong,     "0", "1", "5", "10", "50", "100", "500");
    private static final OneArgument<Integer> INT_ARG =        create(Integer::parseInt,   "0", "1", "5", "10", "50", "100", "500");
    private static final OneArgument<Short> SHORT_ARG =        create(Short::parseShort,   "0", "1", "5", "10", "50");
    private static final OneArgument<Byte> BYTE_ARG =          create(Byte::parseByte,     "0", "1", "5", "10", "50");
    private static final OneArgument<Double> DOUBLE_ARG =      create(Double::parseDouble, "0", "1", "1.5", "10", "10.5", "100", "100.5");
    private static final OneArgument<Float> FLOAT_ARG =        create(Float::parseFloat,   "0", "1", "1.5", "10", "10.5", "100", "100.5");
    private static final OneArgument<BigInteger> BIG_INT_ARG = create(BigInteger::new,     "0", "1", "5", "10", "50", "100", "500");
    private static final OneArgument<BigDecimal> BIG_DEC_ARG = create(BigDecimal::new,     "0", "1", "1.5", "10", "10.5", "100", "100.5");

    private static final Redirector<Schematic, String> MAP_SCHEMATIC_TO_STRING = schematic -> String.join(System.lineSeparator(), schematic.getSchematics());
    private static final Redirector<RequiredPermissions, String> MAP_PERMISSIONS_TO_STRING = permissions -> String.join(System.lineSeparator(), permissions.getPermissions());

    private LiteFactory() {
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

                .argument(String.class, STRING_ARG)
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
                .argument(BigInteger.class, BIG_INT_ARG)
                .argument(BigDecimal.class, BIG_DEC_ARG)

                .argument(Arg.class, Enum.class, new EnumArgument<>())

                .resultHandler(boolean.class, (sender, invocation, value) -> {})
                .resultHandler(Boolean.class, (sender, invocation, value) -> {})

                .redirectResult(Schematic.class, String.class, MAP_SCHEMATIC_TO_STRING)
                .redirectResult(RequiredPermissions.class, String.class, MAP_PERMISSIONS_TO_STRING)

                .contextualBind(LiteInvocation.class, (sender, invocation) -> Result.ok(invocation.toLite()))
                .contextualBind(LiteSender.class, (sender, invocation) -> Result.ok(invocation.sender()))
                .contextualBind(senderType, (sender, invocation) -> Result.ok(sender))

                .annotatedBind(String[].class, Args.class, (invocation, parameter, annotation) -> invocation.arguments())
                .annotatedBind(List.class, Args.class, (invocation, parameter, annotation) -> Arrays.asList(invocation.arguments()))

                .contextualBind(String[].class, (sender, invocation) -> {
                    Logger.getLogger("LiteCommands").warning("Add @Args to String[] parameter in command " + invocation.name());

                    return Result.ok(invocation.arguments());
                })
                ;

    }

    private static <T> OneArgument<T> create(Function<String, T> parse, String... suggestions) {
        return OneArgument.create(
                (inv, arg) -> parse(parse, arg),
                inv -> {
                    List<Suggestion> parsedSuggestions = new ArrayList<>(of(suggestions));
                    Optional<Suggestion> optionalSuggestion = inv.argument(inv.arguments().length - 1)
                            .filter(arg -> !arg.isEmpty())
                            .map(Suggestion::of);

                    optionalSuggestion.ifPresent(parsedSuggestions::add);

                    return parsedSuggestions;
                },
                (inv, suggestion) -> validate(parse, suggestion)
        );
    }

    private static <T> Result<T, Blank> parse(Function<String, T> parse, String value) {
        return Result.supplyThrowing(NumberFormatException.class, () -> parse.apply(value)).mapErrToBlank();
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
