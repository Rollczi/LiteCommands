package dev.rollczi.litecommands.scheme;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.command.section.CommandSection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class SimpleSchemeFormat implements SchemeFormat {

    private final String slash;
    private final Function<CommandSection<?>, String> command;
    private final Function<CommandSection<?>, String> subcommand;
    private final Function<List<CommandSection<?>>, String> subcommands;
    private final Function<AnnotatedParameter<?, ?>, String> argument;
    private final Function<AnnotatedParameter<?, ?>, String> optionalArgument;

    SimpleSchemeFormat(
            String slash,
            Function<CommandSection<?>, String> command,
            Function<CommandSection<?>, String> subcommand,
            Function<List<CommandSection<?>>, String> subcommands,
            Function<AnnotatedParameter<?, ?>, String> argument,
            Function<AnnotatedParameter<?, ?>, String> optionalArgument
    ) {
        this.slash = slash;
        this.command = command;
        this.subcommand = subcommand;
        this.subcommands = subcommands;
        this.argument = argument;
        this.optionalArgument = optionalArgument;
    }

    @Override
    public String slash() {
        return this.slash;
    }

    @Override
    public String command(CommandSection<?> command) {
        return this.command.apply(command);
    }

    @Override
    public String subcommand(CommandSection<?> subcommand) {
        return this.subcommand.apply(subcommand);
    }

    @Override
    public String subcommands(List<? extends CommandSection<?>> subcommands) {
        return this.subcommands.apply(new ArrayList<>(subcommands));
    }

    @Override
    public String argument(AnnotatedParameter<?, ?> state) {
        return state.argument().isOptional() ? this.optionalArgument.apply(state) : this.argument.apply(state);
    }

}
