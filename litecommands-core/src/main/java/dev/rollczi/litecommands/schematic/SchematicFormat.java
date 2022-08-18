package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.command.section.CommandSection;

import java.util.List;

public interface SchematicFormat {

    String slash();

    String command(CommandSection<?> command);

    String subcommand(CommandSection<?> subcommand);

    String subcommands(List<? extends CommandSection<?>> subcommands);

    String argument(AnnotatedParameter<?, ?> argument);

    SchematicFormat ARGUMENT_ANGLED_OPTIONAL_SQUARE = new SchematicFormatBuilder()
            .slash("/")
            .subcommands("|")
            .argument(Brackets.ANGLED)
            .optionalArgument(Brackets.SQUARE)
            .build();

    SchematicFormat ARGUMENT_SQUARE_OPTIONAL_ANGLED = new SchematicFormatBuilder()
            .slash("/")
            .subcommands("|")
            .argument(Brackets.SQUARE)
            .optionalArgument(Brackets.ANGLED)
            .build();

}
