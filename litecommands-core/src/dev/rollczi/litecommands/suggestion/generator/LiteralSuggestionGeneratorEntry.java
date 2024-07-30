package dev.rollczi.litecommands.suggestion.generator;

import dev.rollczi.litecommands.suggestion.SuggestionResult;

public class LiteralSuggestionGeneratorEntry extends RangedSuggestionGeneratorEntry implements SuggestionGeneratorEntry {

    private final String literal;

    public LiteralSuggestionGeneratorEntry(String literal) {
        super(literal.length());
        this.literal = literal;
    }

    @Override
    protected boolean isApplicable(String rightRest) {
        return literal.startsWith(rightRest);
    }

    @Override
    public SuggestionResult generate(String left, String rightRest) {
        return SuggestionResult.of(left + literal);
    }

}
