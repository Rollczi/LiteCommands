package dev.rollczi.litecommands.suggestion.generator;

import dev.rollczi.litecommands.input.raw.RawInputView;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Test;

class SuggestionGeneratorTest {

    public static final SuggestionGenerator GENERATOR = new SuggestionGeneratorBuilder()
        .digits(2)
        .literal("-")
        .digits(2)
        .build();

    @Test
    void test() {
        SuggestionResult result = GENERATOR.generate(RawInputView.of(""));

        assertThat(result.getSuggestions())
            .map(suggestion -> suggestion.multilevel())
            .containsExactlyInAnyOrder("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    }

    @Test
    void test2() {
        SuggestionResult result = GENERATOR.generate(RawInputView.of("1"));

        assertThat(result.getSuggestions())
            .map(suggestion -> suggestion.multilevel())
            .containsExactlyInAnyOrder("10", "11", "12", "13", "14", "15", "16", "17", "18", "19");
    }

    @Test
    void test3() {
        SuggestionResult result = GENERATOR.generate(RawInputView.of("1-"));

        assertThat(result.getSuggestions())
            .isEmpty();
    }

    @Test
    void test4() {
        SuggestionResult result = GENERATOR.generate(RawInputView.of("12"));

        assertThat(result.getSuggestions())
            .map(suggestion -> suggestion.multilevel())
            .containsExactlyInAnyOrder("12-");
    }

    @Test
    void test5() {
        SuggestionResult result = GENERATOR.generate(RawInputView.of("12-"));

        assertThat(result.getSuggestions())
            .map(suggestion -> suggestion.multilevel())
            .containsExactlyInAnyOrder("12-0", "12-1", "12-2", "12-3", "12-4", "12-5", "12-6", "12-7", "12-8", "12-9");
    }

}