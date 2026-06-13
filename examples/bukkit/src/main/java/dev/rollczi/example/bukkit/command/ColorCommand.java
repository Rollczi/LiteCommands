package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.CaseInsensitive;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;

@Command(name = "color", aliases = "c")
public class ColorCommand {

    /**
     * This command is case-insensitive because of the @CaseInsensitive annotation.
     * Example: /color-insensitive RED -> success
     * Example: /color-insensitive red -> success
     */
    @Execute
    String executeCaseInsensitive(@Arg @CaseInsensitive Color color) {
        return "Color set to: " + color.name();
    }

    /**
     * This command is case-sensitive by default.
     * Example: /color-sensitive RED -> success
     * Example: /color-sensitive red -> failure
     */
    @Execute
    String executeCaseSensitive(@Arg Color color) {
        return "Color set to: " + color.name();
    }

    private enum Color {
        RED,
        GREEN,
        BLUE
    }
}
