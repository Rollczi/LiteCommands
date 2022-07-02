package dev.rollczi.litecommands;

import dev.rollczi.litecommands.sugesstion.SuggestionStack;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class AssertSuggest {

    private final SuggestionStack suggestions;

    public AssertSuggest(SuggestionStack suggestions) {
        this.suggestions = suggestions;
    }

    public void assertEmpty() {
        Assertions.assertTrue(suggestions.suggestions().isEmpty());
    }

    public void assertWith(String... suggestions) {
        List<String> suggestionSet = this.suggestions.multilevelSuggestions();

        Assertions.assertEquals(suggestionSet.size(), suggestions.length);

        for (String suggestion : suggestions) {
            Assertions.assertTrue(suggestionSet.contains(suggestion), "Suggestion '" + suggestion + "' not found! " + String.join(", ", suggestionSet));
        }
    }

}
