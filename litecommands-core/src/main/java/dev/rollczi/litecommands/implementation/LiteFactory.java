package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Args;
import dev.rollczi.litecommands.argument.basictype.BigDecimalArgument;
import dev.rollczi.litecommands.argument.basictype.BigIntegerArgument;
import dev.rollczi.litecommands.argument.basictype.BooleanArgument;
import dev.rollczi.litecommands.argument.basictype.ByteArgument;
import dev.rollczi.litecommands.argument.basictype.CharacterArgument;
import dev.rollczi.litecommands.argument.basictype.DoubleArgument;
import dev.rollczi.litecommands.argument.basictype.time.DurationArgument;
import dev.rollczi.litecommands.argument.basictype.FloatArgument;
import dev.rollczi.litecommands.argument.basictype.time.DayOfWeekArgument;
import dev.rollczi.litecommands.argument.basictype.time.HijrahDateArgument;
import dev.rollczi.litecommands.argument.basictype.time.InstantArgument;
import dev.rollczi.litecommands.argument.basictype.IntegerArgument;
import dev.rollczi.litecommands.argument.basictype.LongArgument;
import dev.rollczi.litecommands.argument.basictype.ShortArgument;
import dev.rollczi.litecommands.argument.basictype.StringArgument;
import dev.rollczi.litecommands.argument.basictype.time.JapaneseDateArgument;
import dev.rollczi.litecommands.argument.basictype.time.LocalDateArgument;
import dev.rollczi.litecommands.argument.basictype.time.LocalDateTimeArgument;
import dev.rollczi.litecommands.argument.basictype.time.LocalTimeArgument;
import dev.rollczi.litecommands.argument.basictype.time.MinguoDateArgument;
import dev.rollczi.litecommands.argument.basictype.time.MonthArgument;
import dev.rollczi.litecommands.argument.basictype.time.MonthDayArgument;
import dev.rollczi.litecommands.argument.basictype.time.OffsetDateTimeArgument;
import dev.rollczi.litecommands.argument.basictype.time.OffsetTimeArgument;
import dev.rollczi.litecommands.argument.basictype.time.PeriodArgument;
import dev.rollczi.litecommands.argument.basictype.time.ThaiBuddhistDateArgument;
import dev.rollczi.litecommands.argument.basictype.time.YearArgument;
import dev.rollczi.litecommands.argument.basictype.time.YearMonthArgument;
import dev.rollczi.litecommands.argument.basictype.time.ZoneOffsetArgument;
import dev.rollczi.litecommands.argument.basictype.time.ZonedDateTimeArgument;
import dev.rollczi.litecommands.argument.block.Block;
import dev.rollczi.litecommands.argument.block.BlockArgument;
import dev.rollczi.litecommands.argument.enumeration.EnumArgument;
import dev.rollczi.litecommands.argument.flag.Flag;
import dev.rollczi.litecommands.argument.flag.FlagArgument;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.argument.joiner.JoinerArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.amount.Between;
import dev.rollczi.litecommands.command.amount.Max;
import dev.rollczi.litecommands.command.amount.Min;
import dev.rollczi.litecommands.command.amount.Required;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.ExecutedPermission;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.handle.Redirector;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.litecommands.schematic.Schematic;
import panda.std.Result;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Arrays;
import java.util.List;

public final class LiteFactory {

    private static final Redirector<Schematic, String> MAP_SCHEMATIC_TO_STRING = schematic -> String.join(System.lineSeparator(), schematic.getSchematics());
    private static final Redirector<RequiredPermissions, String> MAP_PERMISSIONS_TO_STRING = permissions -> String.join(System.lineSeparator(), permissions.getPermissions());


    private LiteFactory() {
    }

    public static <SENDER> LiteCommandsBuilder<SENDER> builder(Class<SENDER> senderType) {
        return LiteCommandsBuilderImpl.builder(senderType)
            .configureFactory(factory -> {
                factory.annotationResolver(Section.RESOLVER);
                factory.annotationResolver(Route.RESOLVER);
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
            .argument(Arg.class, Enum.class, new EnumArgument<>())

            .argument(String.class, new StringArgument())
            .argument(boolean.class, new BooleanArgument())
            .argument(Boolean.class, new BooleanArgument())
            .argument(long.class, new LongArgument())
            .argument(Long.class, new LongArgument())
            .argument(int.class, new IntegerArgument())
            .argument(Integer.class, new IntegerArgument())
            .argument(short.class, new ShortArgument())
            .argument(Short.class, new ShortArgument())
            .argument(byte.class, new ByteArgument())
            .argument(Byte.class, new ByteArgument())
            .argument(double.class, new DoubleArgument())
            .argument(Double.class, new DoubleArgument())
            .argument(float.class, new FloatArgument())
            .argument(Float.class, new FloatArgument())
            .argument(char.class, new CharacterArgument())
            .argument(Character.class, new CharacterArgument())
            .argument(BigInteger.class, new BigIntegerArgument())
            .argument(BigDecimal.class, new BigDecimalArgument())

            .argumentMultilevel(Duration.class, new DurationArgument())
            .argumentMultilevel(Period.class, new PeriodArgument())

            .argumentMultilevel(Instant.class, new InstantArgument())
            .argumentMultilevel(OffsetDateTime.class, new OffsetDateTimeArgument())
            .argumentMultilevel(ZonedDateTime.class, new ZonedDateTimeArgument())
            .argumentMultilevel(OffsetTime.class, new OffsetTimeArgument())

            .argumentMultilevel(LocalDate.class, new LocalDateArgument())
            .argumentMultilevel(LocalTime.class, new LocalTimeArgument())
            .argumentMultilevel(LocalDateTime.class, new LocalDateTimeArgument())
            .argumentMultilevel(Year.class, new YearArgument())
            .argumentMultilevel(YearMonth.class, new YearMonthArgument())
            .argumentMultilevel(MonthDay.class, new MonthDayArgument())
            .argumentMultilevel(Month.class, new MonthArgument())
            .argumentMultilevel(DayOfWeek.class, new DayOfWeekArgument())
            .argumentMultilevel(ZoneOffset.class, new ZoneOffsetArgument())

            .argumentMultilevel(JapaneseDate.class, new JapaneseDateArgument())
            .argumentMultilevel(HijrahDate.class, new HijrahDateArgument())
            .argumentMultilevel(MinguoDate.class, new MinguoDateArgument())
            .argumentMultilevel(ThaiBuddhistDate.class, new ThaiBuddhistDateArgument())


            .resultHandler(boolean.class, (sender, invocation, value) -> {})
            .resultHandler(Boolean.class, (sender, invocation, value) -> {})

            .redirectResult(Schematic.class, String.class, MAP_SCHEMATIC_TO_STRING)
            .redirectResult(RequiredPermissions.class, String.class, MAP_PERMISSIONS_TO_STRING)

            .contextualBind(LiteInvocation.class, (sender, invocation) -> Result.ok(invocation.toLite()))
            .contextualBind(LiteSender.class, (sender, invocation) -> Result.ok(invocation.sender()))
            .contextualBind(senderType, (sender, invocation) -> Result.ok(sender))

            .annotatedBind(String[].class, Args.class, (invocation, parameter, annotation) -> invocation.arguments())
            .annotatedBind(List.class, Args.class, (invocation, parameter, annotation) -> Arrays.asList(invocation.arguments()))
            ;

    }

}
