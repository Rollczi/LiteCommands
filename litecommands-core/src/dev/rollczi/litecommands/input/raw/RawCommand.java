package dev.rollczi.litecommands.input.raw;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RawCommand {

    public static final String COMMAND_SLASH = "/";
    public static final String COMMAND_SEPARATOR = " ";
    public static final char COMMAND_SEPARATOR_CHAR = ' ';

    private final String label;
    private final List<String> args;

    private RawCommand(String label, List<String> args) {
        this.label = label;
        this.args = args;
    }

    public String getLabel() {
        return label;
    }

    public RawInput toRawInput() {
        return RawInput.of(args);
    }

    public SuggestionInput<?> toSuggestionInput() {
        return SuggestionInput.raw(args);
    }

    public ParseableInput<?> toParseableInput() {
        return ParseableInput.raw(args);
    }

    public List<String> getArgs() {
        return args;
    }

    public static RawCommand from(String rawInput) {
        String rawCommand = rawInput.startsWith(COMMAND_SLASH)
            ? rawInput.substring(COMMAND_SLASH.length())
            : rawInput;

        String[] rawCommandParts = rawCommand.split(COMMAND_SEPARATOR);
        String commandLabel = rawCommandParts[0];

        List<String> commandArgs = new ArrayList<>();

        if (rawCommandParts.length > 1) {
            commandArgs.addAll(Arrays.asList(rawCommandParts).subList(1, rawCommandParts.length));
        }

        if (rawCommand.endsWith(COMMAND_SEPARATOR)) {
            commandArgs = new ArrayList<>(commandArgs);
            commandArgs.add(StringUtil.EMPTY);
        }

        return new RawCommand(commandLabel, commandArgs);
    }

}
