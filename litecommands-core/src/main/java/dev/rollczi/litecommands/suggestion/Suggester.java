package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.command.LiteInvocation;

import java.util.Arrays;
import java.util.List;

public interface Suggester {

    UniformSuggestionStack suggestion();

    default boolean validate(Suggestion suggestion) {
        return false;
    }

    default SuggesterResult extractSuggestions(int route, LiteInvocation invocation) {
        String[] rawArguments = invocation.arguments();
        UniformSuggestionStack stack = this.suggestion();
        int end = Math.min(route + stack.lengthMultilevel(), rawArguments.length);

        if (route > end) {
            return new SuggesterResult(stack, true);
        }

        List<String> multilevelArguments = Arrays.asList(rawArguments).subList(route, end);

        if (multilevelArguments.isEmpty()) {
            return new SuggesterResult(stack, true);
        }

        String multilevel = String.join(" ", multilevelArguments)
                .toLowerCase();

        UniformSuggestionStack suggestionStack = UniformSuggestionStack.empty(stack.lengthMultilevel());

        boolean isLast = end == rawArguments.length;

        for (Suggestion suggestion : stack.suggestions()) {
            if ((isLast && suggestion.multilevel().toLowerCase().startsWith(multilevel)) || suggestion.multilevel().equalsIgnoreCase(multilevel)) {
                suggestionStack = suggestionStack.with(suggestion);
            }
        }

        return new SuggesterResult(suggestionStack, !suggestionStack.isEmpty() || this.validate(Suggestion.multilevel(multilevelArguments)));
    }

}
