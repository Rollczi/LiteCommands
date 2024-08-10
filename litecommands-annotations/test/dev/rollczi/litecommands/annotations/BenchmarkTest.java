package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

class BenchmarkTest extends LiteTestSpec {

    @Command(name = "command")
    static class TestCommand {

        @Execute(name = "BigDecimal") void test(@Arg BigDecimal bigDecimal) {}
        @Execute(name = "BigInteger") void test(@Arg BigInteger bigInteger) {}
        @Execute(name = "Boolean") void test(@Arg Boolean bool) {}
        @Execute(name = "boolean") void test(@Arg boolean bool) {}
        @Execute(name = "Byte") void test(@Arg Byte b) {}
        @Execute(name = "byte") void test(@Arg byte b) {}
        @Execute(name = "Character") void test(@Arg Character c) {}
        @Execute(name = "char") void test(@Arg char c) {}
        @Execute(name = "Double") void test(@Arg Double d) {}
        @Execute(name = "double") void test(@Arg double d) {}
        @Execute(name = "int") void test(@Arg int i) {}
        @Execute(name = "integer") void test(@Arg Integer integer) {}
        @Execute(name = "Long") void test(@Arg Long l) {}
        @Execute(name = "long") void test(@Arg long l) {}
        @Execute(name = "Short") void test(@Arg Short s) {}
        @Execute(name = "short") void test(@Arg short s) {}
        @Execute(name = "Duration") void test(@Arg Duration duration) {}
        @Execute(name = "Instant") void test(@Arg Instant instant) {}
        @Execute(name = "LocalDateTime") void test(@Arg LocalDateTime localDateTime) {}
        @Execute(name = "Period") void test(@Arg Period period) {}
        @Execute(name = "String") void test(@Arg String string) {}
        @Execute(name = "Enum") void test(@Arg RetentionPolicy retentionPolicy) {}
        @Execute(name = "durations") void test(@Arg List<Duration> durations) {}
        @Execute(name = "durations-instant") void test(@Varargs(delimiter = ",") List<Duration> durations, @Arg Instant instant) {}
        @Execute(name = "instants-duration") void test(@Varargs(delimiter = ", ") List<Instant> instants, @Arg Duration duration) {}

    }

    @Disabled
    @Test
    void test() {
        for (int i = 0; i < 10_000; i++) {
            runCommand("command",
                // missing sub command
                "", "d", "D", "l", "L",
                // missing argument
                "BigDecimal",
                "BigInteger",
                "Boolean",
                "boolean",
                "Byte",
                "byte",
//                "Character",
//                "char",
                "Double",
                "double",
                "int",
                "integer",
                "Long",
                "long",
                "Short",
                "short",
                "Duration",
                "Instant",
                "LocalDateTime",
                "Period",
                "String",
                "Enum",
                "durations",
                "durations-instant",
                "instants-duration",

                "BigDecimal 100.123456789123456",
                "BigInteger 100123123124243535634634563456",
                "Boolean true",
                "boolean false",
                "Byte 12",
                "byte 50",
//                "Character a",
//                "char Z",
                "Double 13.0D",
                "double 304.444",
                "int 350",
                "integer 420",
                "Long 12345467",
                "long 12344567",
                "Short 234",
                "short 130",
                "Duration 30m15s",
                "Duration 15d3h30m15s",
                "Instant 2023-04-17 00:05:50",
                "LocalDateTime 2023-04-13 00:15:50",
                "Period 2y1mo",
                "String text",
                "Enum RUNTIME",
                "durations 10s 30h 15m",
                "durations-instant 30m15s,15d3h30m15s 2023-04-17 00:05:50",
                "instants-duration 2023-04-17 00:05:50, 2023-04-17 00:05:50 30s",


                "BigDecimal ",
                "BigInteger ",
                "Boolean tr",
                "boolean fa",
                "Byte ",
                "byte ",
//                "Character ",
//                "char ",
                "Double ",
                "double ",
                "int ",
                "integer ",
                "Long ",
                "long ",
                "Short ",
                "short ",
                "Duration 10",
                "Duration 5s",
                "Instant 2023-",
                "Instant 2023-04-",
                "Instant 2023-04-17",
                "Instant 2023-04-17 ",
                "Instant 2023-04-17 00:",
                "LocalDateTime 2023-",
                "LocalDateTime 2023-04-",
                "LocalDateTime 2023-04-17",
                "LocalDateTime 2023-04-17 ",
                "LocalDateTime 2023-04-17 00:",
                "Period 1",
                "String ",
                "Enum ",
                "durations 10s ",
                "durations-instant 30m15s,15d3h",
                "durations-instant 30m15s,15d3h30m15s 2023-04-17 ",
                "instants-duration 2023-04-17 00:05:50",
//                "instants-duration 2023-04-17 00:05:50,",
                "instants-duration 2023-04-17 00:05:50, 2023-04-17 00:05:50 ",
                "instants-duration 2023-04-17 00:05:50, 2023-04-17 00:05:50 30s"
            );
        }
    }

    private void runCommand(String command, String... subcommands) {
        platform.execute(command);
        platform.suggest(command );
        for (String subcommand : subcommands) {
            try {

                platform.execute(command + " " + subcommand);
                platform.suggest(command + " " + subcommand);
            } catch (Exception exception) {
                System.out.println("Failed: /" + command + " " + subcommand);
            }
        }
    }

}
