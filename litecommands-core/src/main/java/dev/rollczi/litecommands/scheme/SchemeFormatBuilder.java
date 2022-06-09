package dev.rollczi.litecommands.scheme;

import dev.rollczi.litecommands.command.section.CommandSection;

import java.util.stream.Collectors;

public class SchemeFormatBuilder {

    private String slash = "/";
    private Brackets command = Brackets.NONE;
    private Brackets subcommand = Brackets.NONE;
    private String subcommands = "|";
    private Brackets subcommandsBrackets = Brackets.NONE;
    private Brackets argument = Brackets.ANGLED;
    private Brackets optionalArgument = Brackets.SQUARE;

    public SchemeFormatBuilder slash(String slash) {
        this.slash = slash;
        return this;
    }

    public SchemeFormatBuilder command(Brackets command) {
        this.command = command;
        return this;
    }

    public SchemeFormatBuilder subcommand(Brackets subcommand) {
        this.subcommand = subcommand;
        return this;
    }

    public SchemeFormatBuilder subcommands(String subcommands) {
        this.subcommands = subcommands;
        return this;
    }

    public SchemeFormatBuilder subcommands(Brackets subcommandsBrackets) {
        this.subcommandsBrackets = subcommandsBrackets;
        return this;
    }

    public SchemeFormatBuilder argument(Brackets argument) {
        this.argument = argument;
        return this;
    }

    public SchemeFormatBuilder optionalArgument(Brackets optionalArgument) {
        this.optionalArgument = optionalArgument;
        return this;
    }

    public SchemeFormat build() {
        return new SimpleSchemeFormat(
                this.slash,
                section -> this.command.close(section.getName()),
                section -> this.subcommand.close(section.getName()),
                commandSections -> this.subcommandsBrackets.close(commandSections.stream().map(CommandSection::getName).collect(Collectors.joining(this.subcommands))),
                argument -> argument.schematic().orElseGet(() -> this.argument.close(argument.name().orElseGet("none"))),
                argument -> argument.schematic().orElseGet(() -> this.optionalArgument.close(argument.name().orElseGet("none")))
        );
    }

}
