package dev.rollczi.litecommands.command.sugesstion;

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
    public UniformSuggestionStack with(Iterable<Suggestion> suggestions) {
        ArrayList<Suggestion> list = new ArrayList<>(this.suggestions);

        for (Suggestion suggestion : suggestions) {
            list.add(suggestion);
        }

        return of(list);
    }

    public static UniformSuggestionStack empty() {
        return new UniformSuggestionStack(Collections.emptyList(), 0);
    }

    public static UniformSuggestionStack of(Collection<Suggestion> suggestions) {
        int last = - 1;

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
