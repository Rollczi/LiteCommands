package dev.rollczi.litecommands.scheme;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentState;
import dev.rollczi.litecommands.command.section.CommandSection;
import panda.utilities.text.Joiner;

import java.util.List;

public interface SchemeFormat {

    String slash();

    String command(CommandSection command);

    String subcommand(CommandSection subcommand);

    String subcommands(List<CommandSection> subcommands);

    String argument(ArgumentState argument);

    SchemeFormat ARGUMENT_ANGLED_OPTIONAL_SQUARE = new SchemeFormatBuilder()
            .slash("/")
            .subcommands("|")
            .argument(Brackets.ANGLED)
            .optionalArgument(Brackets.SQUARE)
            .build();

    SchemeFormat ARGUMENT_SQUARE_OPTIONAL_ANGLED = new SchemeFormatBuilder()
            .slash("/")
            .subcommands("|")
            .argument(Brackets.SQUARE)
            .optionalArgument(Brackets.ANGLED)
            .build();

}
