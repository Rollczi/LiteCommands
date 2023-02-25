package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.command.section.CommandSection;

import java.util.stream.Collectors;

public class SchematicFormatBuilder {

    private String slash = "/";
    private Brackets command = Brackets.NONE;
    private Brackets subcommand = Brackets.NONE;
    private String subcommands = "|";
    private Brackets subcommandsBrackets = Brackets.NONE;
    private Brackets argument = Brackets.ANGLED;
    private Brackets optionalArgument = Brackets.SQUARE;

    public SchematicFormatBuilder slash(String slash) {
        this.slash = slash;
        return this;
    }

    public SchematicFormatBuilder command(Brackets command) {
        this.command = command;
        return this;
    }

    public SchematicFormatBuilder subcommand(Brackets subcommand) {
        this.subcommand = subcommand;
        return this;
    }

    public SchematicFormatBuilder subcommands(String subcommands) {
        this.subcommands = subcommands;
        return this;
    }

    public SchematicFormatBuilder subcommands(Brackets subcommandsBrackets) {
        this.subcommandsBrackets = subcommandsBrackets;
        return this;
    }

    public SchematicFormatBuilder argument(Brackets argument) {
        this.argument = argument;
        return this;
    }

    public SchematicFormatBuilder optionalArgument(Brackets optionalArgument) {
        this.optionalArgument = optionalArgument;
        return this;
    }

    public SchematicFormat build() {
        return new SimpleSchematicFormat(
                this.slash,
                section -> this.command.close(section.getName()),
                section -> this.subcommand.close(section.getName()),
                commandSections -> this.subcommandsBrackets.close(commandSections.stream().map(CommandSection::getName).collect(Collectors.joining(this.subcommands))),
                argument -> argument.schematic().orElseGet(() -> this.argument.close(argument.name())),
                argument -> argument.schematic().orElseGet(() -> this.optionalArgument.close(argument.name()))
        );
    }

}
