package dev.rollczi.litecommands.suggestion.generator;

import dev.rollczi.litecommands.suggestion.SuggestionResult;

public class DigitsSuggestionGeneratorEntry extends RangedSuggestionGeneratorEntry implements SuggestionGeneratorEntry {

    public static final String[] SUGGESTIONS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    protected DigitsSuggestionGeneratorEntry(int length) {
        super(length);
    }

    @Override
    protected boolean isApplicable(String rightRest) {
        return rightRest.matches("\\d+");
    }

    @Override
    public SuggestionResult generate(String left, String rightRest) {
        return SuggestionResult.of(SUGGESTIONS)
            .appendLeftDirectly(left + rightRest);
    }

}
