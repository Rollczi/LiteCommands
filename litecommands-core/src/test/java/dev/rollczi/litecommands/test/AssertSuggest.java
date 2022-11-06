package dev.rollczi.litecommands.test;

import dev.rollczi.litecommands.suggestion.SuggestionStack;
import org.junit.jupiter.api.Assertions;
import panda.std.stream.PandaStream;

import java.util.List;

public class AssertSuggest {

    private final SuggestionStack suggestions;

    public AssertSuggest(SuggestionStack suggestions) {
        this.suggestions = suggestions;
    }

    public void assertEmpty() {
        Assertions.assertTrue(suggestions.suggestions().isEmpty());
    }

    public void assertNonEmpty() {
        Assertions.assertFalse(suggestions.suggestions().isEmpty());
    }

    public void assertWith(String... suggestions) {
        List<String> suggestionSet = this.suggestions.multilevelSuggestions();

        Assertions.assertEquals(suggestionSet.size(), suggestions.length);

        for (String suggestion : suggestions) {
            Assertions.assertTrue(suggestionSet.contains(suggestion), "Suggestion '" + suggestion + "' not found! " + String.join(", ", suggestionSet));
        }
    }

    public void assertWith(String[] suggestionArray, String... suggestions) {
        String[] merged = PandaStream.of(suggestionArray)
                .concat(suggestions)
                .toList()
                .toArray(new String[0]);

        assertWith(merged);
    }

}
