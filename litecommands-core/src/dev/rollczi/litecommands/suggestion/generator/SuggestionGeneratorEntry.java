package dev.rollczi.litecommands.suggestion.generator;

import dev.rollczi.litecommands.input.raw.RawInputView;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface SuggestionGeneratorEntry {

    SuggestionGeneratorResult read(RawInputView view);

    /**
     * Generates a suggestion based on the context and the input
     *
     * @param left      - processed left part of the input by other entries in the chain (e.g. 12:30:, 12:30:)
     * @param rightRest - unprocessed left part of the input (e.g. 4, 45)
     * @return - suggestion result
     */
    SuggestionResult generate(String left, String rightRest);

}
