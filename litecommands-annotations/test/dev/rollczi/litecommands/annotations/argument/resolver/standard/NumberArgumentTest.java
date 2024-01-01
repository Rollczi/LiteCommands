package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class NumberArgumentTest extends LiteTestSpec {

    @Command(name = "number")
    static class NumberCommand {

        @Execute(name = "byte")
        String byteCommand(@Arg byte number) {
            return "byte: " + number;
        }

        @Execute(name = "short")
        String shortCommand(@Arg short number) {
            return "short: " + number;
        }

        @Execute(name = "int")
        String intCommand(@Arg int number) {
            return "int: " + number;
        }

        @Execute(name = "long")
        String longCommand(@Arg long number) {
            return "long: " + number;
        }

        @Execute(name = "float")
        String floatCommand(@Arg float number) {
            return "float: " + number;
        }

        @Execute(name = "double")
        String doubleCommand(@Arg double number) {
            return "double: " + number;
        }

    }

    @Test
    void testByte() {
        platform.execute("number byte 10")
            .assertSuccess("byte: 10");

        platform.execute("number byte 10.5")
            .assertFailure();

        platform.suggest("number byte 12")
            .assertSuggest("12", "120", "121", "122", "123", "124", "125", "126", "127");
    }

    @ParameterizedTest
    @CsvSource({"short", "int", "long"})
    void testNumbers(String type) {
        platform.execute("number " + type + " 10")
            .assertSuccess(type + ": 10");

        platform.execute("number " + type + " 10.5")
            .assertFailure();

        platform.suggest("number " + type + " ")
            .assertSuggest("-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "50", "100", "500");

        platform.suggest("number " + type + " -")
            .assertSuggest("-0", "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9");

        platform.suggest("number " + type + " 1")
            .assertSuggest("10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "100");
    }

    @ParameterizedTest
    @CsvSource({"float", "double"})
    void testDecimals(String type) {
        platform.execute("number " + type + " 10")
            .assertSuccess(type + ": 10.0");

        platform.execute("number " + type + " 10.5")
            .assertSuccess(type + ": 10.5");

        platform.execute("number " + type + " -.5")
            .assertSuccess(type + ": -0.5");

        platform.suggest("number " + type + " ")
            .assertSuggest("-", ".", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

        platform.suggest("number " + type + " -")
            .assertSuggest("-.", "-0", "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9");

        platform.suggest("number " + type + " 1")
            .assertSuggest("1.", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19");
    }




}