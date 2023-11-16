package dev.rollczi.litecommands.argument.suggester;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SuggesterRegistryImplTest {

    @Test
    void registerSuggester() {
        SuggesterRegistryImpl<TestSender> registry = new SuggesterRegistryImpl<>();

        registry.registerSuggester(String.class, ArgumentKey.of(), new NamedSuggester("universal"));

        Suggester<TestSender, String> universal = registry.getSuggester(String.class, ArgumentKey.of());
        SuggestionResult suggest = universal.suggest(null, null, null);

        assertSuggestion("universal", suggest);
    }

    @Test
    void registerCustomSuggester() {
        SuggesterRegistryImpl<TestSender> registry = new SuggesterRegistryImpl<>();

        registry.registerSuggester(String.class, ArgumentKey.of("custom"), new NamedSuggester("custom"));
        registry.registerSuggester(String.class, ArgumentKey.of(), new NamedSuggester("universal"));

        Suggester<TestSender, String> custom = registry.getSuggester(String.class, ArgumentKey.of("custom"));
        Suggester<TestSender, String> universal = registry.getSuggester(String.class, ArgumentKey.of());
        Suggester<TestSender, String> missing = registry.getSuggester(String.class, ArgumentKey.of("missing"));

        SuggestionResult customSuggest = custom.suggest(null, null, null);
        SuggestionResult universalSuggest = universal.suggest(null, null, null);
        SuggestionResult missingSuggest = missing.suggest(null, null, null);

        assertSuggestion("custom", customSuggest);
        assertSuggestion("universal", universalSuggest);
        assertSuggestion("universal", missingSuggest);
    }

    @Test
    void registerGenericSuggester() {
        SuggesterRegistryImpl<TestSender> registry = new SuggesterRegistryImpl<>();

        registry.registerSuggester(Number.class, ArgumentKey.of(), new NumberSuggester());

        Suggester<TestSender, Number> universal = registry.getSuggester(Number.class, ArgumentKey.of());
        Suggester<TestSender, Integer> integer = registry.getSuggester(Integer.class, ArgumentKey.of());

        SuggestionResult universalSuggest = universal.suggest(null, null, null);
        SuggestionResult integerSuggest = integer.suggest(null, null, null);

        assertSuggestion("number", universalSuggest);
        assertSuggestion("number", integerSuggest);
    }

    private <T> T assertOptional(Optional<T> optional) {
        assertTrue(optional.isPresent());
        return optional.get();
    }

    private void assertSuggestion(String expected, SuggestionResult result) {
        assertThat(result.getSuggestions())
            .hasSize(1);

        Suggestion suggestion = assertOptional(result.getSuggestions().stream().findFirst());

        assertThat(suggestion.multilevel())
            .isEqualTo(expected);
    }

    private static class NamedSuggester implements Suggester<TestSender, String> {
        private final String name;

        public NamedSuggester(String name) {
            this.name = name;
        }

        @Override
        public SuggestionResult suggest(Invocation<TestSender> invocation, Argument<String> argument, SuggestionContext context) {
            return SuggestionResult.of(name);
        }
    }

    private static class NumberSuggester implements Suggester<TestSender, Number> {
        @Override
        public SuggestionResult suggest(Invocation<TestSender> invocation, Argument<Number> argument, SuggestionContext context) {
            return SuggestionResult.of("number");
        }
    }

}