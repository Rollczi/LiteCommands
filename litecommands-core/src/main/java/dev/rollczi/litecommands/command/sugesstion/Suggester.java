package dev.rollczi.litecommands.command.sugesstion;

import java.util.Collections;
import java.util.List;

public interface Suggester {

    Suggester NONE = Collections::emptyList;

    static Suggester of(Iterable<Suggestion> iterable) {
        return new IteratorSuggester(iterable);
    }

    List<Suggestion> suggestions();

}
