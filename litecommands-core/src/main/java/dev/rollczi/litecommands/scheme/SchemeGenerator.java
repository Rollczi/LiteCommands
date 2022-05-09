package dev.rollczi.litecommands.scheme;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentState;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.section.CommandSection;
import panda.utilities.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchemeGenerator {

    public String generate(FindResult result, SchemeFormat schemeFormat) {
        List<CommandSection> sections = result.getSections();

        if (sections.isEmpty()) {
            return StringUtils.EMPTY;
        }

        CommandSection command = sections.get(0);
        Optional<ArgumentExecutor> executor = result.getExecutor();
        CommandSection lastSection = sections.get(sections.size() - 1);
        List<CommandSection> subcommand = sections.stream().skip(1).collect(Collectors.toList());

        StringBuilder content = new StringBuilder();

        content.append(schemeFormat.slash());
        content.append(schemeFormat.command(command));

        if (executor.isPresent()) {
            for (CommandSection commandSection : subcommand) {
                content.append(" ");
                content.append(schemeFormat.subcommand(commandSection));
            }

            for (ArgumentState argument : result.getArgumentStates()) {
                content.append(" ");
                content.append(schemeFormat.argument(argument));
            }
        }
        else {
            content.append(" ");
            content.append(schemeFormat.subcommands(lastSection.childrenSection()));
        }

        return content.toString();
    }

}
