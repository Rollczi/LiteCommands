package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.suggestion.Suggestion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SuggestionTest {

    @Test
    void testEquals() {
        assertEquals(Suggestion.of("test"), Suggestion.of("test"));
        assertEquals(Suggestion.of("test test2"), Suggestion.of("test test2"));
        assertNotEquals(Suggestion.of("test"), Suggestion.of("test2"));
    }

}