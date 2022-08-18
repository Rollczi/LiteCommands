package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.section.CommandSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class SimpleSchematicGenerator implements SchematicGenerator {

    @Override
    public Schematic generateSchematic(SchematicContext<?> context, SchematicFormat schematicFormat) {
        return new Schematic(this.generate(context, schematicFormat));
    }

    @Override
    public List<String> generate(SchematicContext<?> context, SchematicFormat schematicFormat) {
        Invocation<?> invocation = context.getInvocation();
        List<? extends CommandSection<?>> sections = context.getSections();

        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        Iterator<String> iterator = Arrays.stream(invocation.arguments()).iterator();

        CommandSection<?> command = sections.get(0);
        Optional<? extends ArgumentExecutor<?>> executor = context.getExecutor();
        CommandSection<?> lastSection = sections.get(sections.size() - 1);
        List<CommandSection<?>> subcommand = sections.stream()
                .skip(1)
                .collect(Collectors.toList());

        StringBuilder known = new StringBuilder();

        known.append(schematicFormat.slash());
        known.append(schematicFormat.command(command));

        if (!iterator.hasNext()) {
            return this.unknown(known.toString(), lastSection, schematicFormat);
        }

        if (executor.isPresent()) {
            for (CommandSection<?> commandSection : subcommand) {
                if (!iterator.hasNext()) {
                    return this.unknown(known.toString(), commandSection, schematicFormat);
                }

                iterator.next();
                known.append(" ");
                known.append(schematicFormat.subcommand(commandSection));
            }

            for (AnnotatedParameter<?, ?> argument : context.getAllArguments()) {
                known.append(" ");
                known.append(schematicFormat.argument(argument));
            }
        }
        else {
            known.append(" ");
            known.append(schematicFormat.subcommands(lastSection.childrenSection()));
        }

        return Collections.singletonList(known.toString());
    }

    private List<String> unknown(String text, CommandSection<?> section, SchematicFormat schematicFormat) {
        List<String> schematics = new ArrayList<>();

        for (CommandSection<?> child : section.childrenSection()) {
            schematics.addAll(this.unknown(text + " " + child.getName(), child, schematicFormat));
        }

        for (ArgumentExecutor<?> executor : section.executors()) {
            schematics.add(text + " " + executor(executor, schematicFormat));
        }

        return schematics;
    }

    private String executor(ArgumentExecutor<?> executor, SchematicFormat schematicFormat) {
        return executor.annotatedParameters().stream().map(schematicFormat::argument).collect(Collectors.joining(" "));
    }

}
