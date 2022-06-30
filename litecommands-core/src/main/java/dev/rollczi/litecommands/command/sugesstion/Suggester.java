package dev.rollczi.litecommands.command.sugesstion;

import dev.rollczi.litecommands.command.LiteInvocation;

import java.util.Arrays;

public interface Suggester {

    UniformSuggestionStack suggest();

    default UniformSuggestionStack filterSuggestions(int route, LiteInvocation invocation) {
        String[] rawArguments = invocation.arguments();
        UniformSuggestionStack stack = this.suggest();
        String multilevel = String.join(" ", Arrays.asList(rawArguments).subList(route, Math.min(route  + stack.lengthMultilevel(), rawArguments.length)))
                .toLowerCase();

        if (multilevel.isEmpty()) {
            return stack;
        }

        if (rawArguments.length - route > stack.lengthMultilevel()) {
            UniformSuggestionStack suggestionStack = UniformSuggestionStack.empty();

            for (Suggestion suggestion : stack.suggestions()) {
                if (suggestion.multilevel().toLowerCase().equals(multilevel)) {
                    suggestionStack = suggestionStack.with(suggestion);
                }
            }

            return suggestionStack;
        }

        UniformSuggestionStack suggestionStack = UniformSuggestionStack.empty();

        for (Suggestion suggestion : stack.suggestions()) {
            if (suggestion.multilevel().toLowerCase().startsWith(multilevel)) {
                suggestionStack = suggestionStack.with(suggestion);
            }
        }

        return suggestionStack;
    }

}
