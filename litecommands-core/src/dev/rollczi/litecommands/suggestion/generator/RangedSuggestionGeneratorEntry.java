package dev.rollczi.litecommands.suggestion.generator;

import dev.rollczi.litecommands.input.raw.RawInputView;

public abstract class RangedSuggestionGeneratorEntry implements SuggestionGeneratorEntry {

    protected final int length;

    protected RangedSuggestionGeneratorEntry(int length) {
        this.length = length;
    }

    @Override
    public SuggestionGeneratorResult read(RawInputView view) {
        if (view.length() < this.length) {
            return SuggestionGeneratorResult.generate();
        }

        String claimed = view.claim(0, this.length);

        if (this.isApplicable(claimed)) {
            return SuggestionGeneratorResult.skip(claimed);
        }

        return SuggestionGeneratorResult.failure();
    }

    protected abstract boolean isApplicable(String rightRest);

}
