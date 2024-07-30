package dev.rollczi.litecommands.suggestion.generator;

import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.input.raw.RawInputView;
import dev.rollczi.litecommands.input.raw.RawInputViewLegacyAdapter;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

/**
 * Experimental feature
 * Easy to use tool for generating dynamic suggestions for the argument based on a suggestion context
 */
@ApiStatus.Experimental
public class SuggestionGenerator {

    private final List<SuggestionGeneratorEntry> entries;

    protected SuggestionGenerator(List<SuggestionGeneratorEntry> entries) {
        this.entries = entries;
    }

    public SuggestionResult generate(SuggestionContext context) {
        return generate(RawInputView.of(context.getCurrent().multilevel()));
    }

    public SuggestionResult generate(RawInputView input) {
        StringBuilder builder = new StringBuilder();

        for (SuggestionGeneratorEntry entry : entries) {
            SuggestionGeneratorResult result = entry.read(input);

            if (result.isSkip()) {
                builder.append(result.getSkipSuggestion());
                continue;
            }

            if (result.isFailure()) {
                break;
            }

            if (result.isGenerate()) {
                return entry.generate(builder.toString(), input.content());
            }
        }

        return SuggestionResult.empty();
    }

    public static SuggestionGeneratorBuilder builder() {
        return new SuggestionGeneratorBuilder();
    }

}
