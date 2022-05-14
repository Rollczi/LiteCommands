package dev.rollczi.litecommands.command.sugesstion;

import java.util.ArrayList;
import java.util.List;

class IteratorSuggester implements Suggester {

    private final Iterable<Suggestion> iterable;

    IteratorSuggester(Iterable<Suggestion> iterable) {
        this.iterable = iterable;
    }

    @Override
    public List<Suggestion> suggestions() {
        List<Suggestion> suggestions = new ArrayList<>();

        for (Suggestion suggestion : iterable) {
            suggestions.add(suggestion);
        }

        return suggestions;
    }
}
