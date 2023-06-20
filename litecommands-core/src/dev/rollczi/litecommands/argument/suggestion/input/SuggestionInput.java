package dev.rollczi.litecommands.argument.suggestion.input;

import dev.rollczi.litecommands.input.Input;
import dev.rollczi.litecommands.shared.Preconditions;

import java.util.Arrays;

public interface SuggestionInput<MATCHER extends SuggestionInputMatcher<MATCHER>> extends Input<MATCHER> {

    static SuggestionInput<?> raw(String[] args) {
        for (String rawArgument : args) {
            Preconditions.notNull(rawArgument, "raw argument");
        }

        return new SuggestionInputRawImpl(Arrays.asList(args));
    }

}
