package dev.rollczi.litecommands.suggestion.generator;

import java.util.ArrayList;
import java.util.List;

public class SuggestionGeneratorBuilder {

    private final List<SuggestionGeneratorEntry> entries = new ArrayList<>();

    public SuggestionGeneratorBuilder addEntry(SuggestionGeneratorEntry entry) {
        entries.add(entry);
        return this;
    }

    public SuggestionGeneratorBuilder literal(String literal) {
        return addEntry(new LiteralSuggestionGeneratorEntry(literal));
    }

    public SuggestionGeneratorBuilder digit() {
        return digits(1);
    }

    public SuggestionGeneratorBuilder digits(int count) {
        return addEntry(new DigitsSuggestionGeneratorEntry(count));
    }

    public SuggestionGenerator build() {
        return new SuggestionGenerator(entries);
    }

}
