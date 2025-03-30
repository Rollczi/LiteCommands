package dev.rollczi.litecommands.input.raw;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        return from(rawInput, COMMAND_SLASH);
    }

    public static RawCommand from(String rawInput, String commandPrefix) {
        String rawCommand = rawInput.startsWith(commandPrefix)
            ? rawInput.substring(commandPrefix.length())
            : rawInput;

        List<String> rawCommandParts = StringUtil.splitBySpace(rawCommand);
        String commandLabel = rawCommandParts.get(0);

        List<String> commandArgs = rawCommandParts.size() > 1
            ? rawCommandParts.subList(1, rawCommandParts.size())
            : Collections.emptyList();

        return new RawCommand(commandLabel, commandArgs);
    }

}
