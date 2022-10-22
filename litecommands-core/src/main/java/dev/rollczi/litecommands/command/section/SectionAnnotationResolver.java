package dev.rollczi.litecommands.command.section;

import dev.rollczi.litecommands.factory.CommandState;
import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import panda.std.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class SectionAnnotationResolver implements FactoryAnnotationResolver<Section> {

    @Override
    public Option<CommandState> resolve(Section annotation, CommandState commandState) {
        if (annotation.route().isEmpty()) {
            return Option.none();
        }

        List<String[]> aliasesList = Arrays.stream(annotation.aliases()).map(s -> s.split(" ")).collect(Collectors.toList());
        String[] command = annotation.route().split(" ");

        commandState = commandState.name(command[command.length - 1]);

        for (String[] aliases : aliasesList) {
            commandState = commandState.alias(aliases[command.length - 1]);
        }

        for (int index = 0; index < command.length - 1; index++) {
            List<String> aliases = new ArrayList<>();

            for (String[] alias : aliasesList) {
                aliases.add(alias[index]);
            }

            commandState = commandState.routeBefore(new CommandState.Route(command[index], aliases));
        }

        return Option.of(commandState);
    }

    @Override
    public Class<Section> getAnnotationClass() {
        return Section.class;
    }

}
