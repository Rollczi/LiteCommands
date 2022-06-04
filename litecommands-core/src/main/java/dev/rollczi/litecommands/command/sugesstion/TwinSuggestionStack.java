package dev.rollczi.litecommands.command.sugesstion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class TwinSuggestionStack extends SuggestionStack {

    private final int multilevelLength;

    private TwinSuggestionStack(List<Suggestion> suggestions, int multilevelLength) {
        super(suggestions);
        this.multilevelLength = multilevelLength;
    }

    public int multilevelLength() {
        return multilevelLength;
    }

    @Override
    public TwinSuggestionStack with(Suggestion... suggestions) {
        return this.with(Arrays.asList(suggestions));
    }

    @Override
    public TwinSuggestionStack with(Iterable<Suggestion> suggestions) {
        ArrayList<Suggestion> list = new ArrayList<>(this.suggestions);

        for (Suggestion suggestion : suggestions) {
            list.add(suggestion);
        }

        return of(list);
    }

    public static TwinSuggestionStack empty() {
        return new TwinSuggestionStack(Collections.emptyList(), 0);
    }

    public static TwinSuggestionStack of(Collection<Suggestion> suggestions) {
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

        return new TwinSuggestionStack(new ArrayList<>(suggestions), last);
    }

}
