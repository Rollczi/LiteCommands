package dev.rollczi.litecommands.suggestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class UniformSuggestionStack extends SuggestionStack {

    private final int multilevelLength;

    private UniformSuggestionStack(List<Suggestion> suggestions, int multilevelLength) {
        super(suggestions);
        this.multilevelLength = multilevelLength;
    }

    public int lengthMultilevel() {
        return multilevelLength;
    }

    public boolean isMultilevel() {
        return multilevelLength > 1;
    }

    public UniformSuggestionStack slashLevel(int level) {
        if (this.suggestions.isEmpty()) {
            return this;
        }

        List<Suggestion> slashed = new ArrayList<>();

        for (Suggestion suggestion : this.suggestions) {
            slashed.add(suggestion.slashLevel(level));
        }

        return new UniformSuggestionStack(slashed,  this.multilevelLength - level);
    }

    @Override
    public UniformSuggestionStack with(Suggestion... suggestions) {
        return this.with(Arrays.asList(suggestions));
    }

    @Override
    public UniformSuggestionStack with(Collection<Suggestion> suggestions) {
        for (Suggestion suggestion : suggestions) {
            if (suggestion.lengthMultilevel() != this.multilevelLength) {
                throw new IllegalArgumentException("length of multi-level suggestions must be same!");
            }
        }

        this.suggestions.addAll(suggestions);
        return this;
    }

    public static UniformSuggestionStack empty() {
        return new UniformSuggestionStack(Collections.emptyList(), 0);
    }

    public static UniformSuggestionStack empty(int initialMultilevelLength) {
        return new UniformSuggestionStack(Collections.emptyList(), initialMultilevelLength);
    }

    public static UniformSuggestionStack of(Collection<Suggestion> suggestions) {
        return of(new ArrayList<>(suggestions), 0);
    }

    public static UniformSuggestionStack of(Collection<Suggestion> suggestions, int multilevelLength) {
        int last = multilevelLength == 0 ? - 1 : multilevelLength;

        for (Suggestion suggestion : suggestions) {
            int length = suggestion.lengthMultilevel();

            if (last == - 1) {
                last = length;
                continue;
            }

            if (last != length) {
                throw new IllegalArgumentException("length of multi-level suggestions must be same!");
            }
        }

        if (last == - 1) {
            return empty();
        }

        return new UniformSuggestionStack(new ArrayList<>(suggestions), last);
    }

}
