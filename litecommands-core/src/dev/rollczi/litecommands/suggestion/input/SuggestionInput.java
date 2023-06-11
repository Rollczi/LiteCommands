package dev.rollczi.litecommands.suggestion.input;

import dev.rollczi.litecommands.command.input.Input;
import dev.rollczi.litecommands.util.Preconditions;

import java.util.Arrays;

public interface SuggestionInput<MATCHER extends SuggestionInputMatcher<MATCHER>> extends Input<MATCHER> {

    static SuggestionInput<?> raw(String[] args) {
        for (String rawArgument : args) {
            Preconditions.notNull(rawArgument, "raw argument");
        }

        return new SuggestionInputRawImpl(Arrays.asList(args));
    }

}
