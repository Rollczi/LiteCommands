package dev.rollczi.example.paper.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;

@Command(name = "number")
public class NumberCommand {

    @Execute(name = "byte")
    String byteCommand(@Arg byte number) {
        return "Byte: " + number;
    }

    @Execute(name = "short")
    String shortCommand(@Arg short number) {
        return "Short: " + number;
    }

    @Execute(name = "int")
    String intCommand(@Arg int number) {
        return "Int: " + number;
    }

    @Execute(name = "long")
    String longCommand(@Arg long number) {
        return "Long: " + number;
    }

    @Execute(name = "float")
    String floatCommand(@Arg float number) {
        return "Float: " + number;
    }

    @Execute(name = "double")
    String doubleCommand(@Arg double number) {
        return "Double: " + number;
    }

}
