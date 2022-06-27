package dev.rollczi.litecommands.command.sugesstion;

import java.util.List;

public interface Suggester {

    Suggester NONE = UniformSuggestionStack::empty;

    static Suggester of(Iterable<Suggestion> iterable) {
        return new IteratorSuggester(iterable);
    }

    UniformSuggestionStack suggest();

    default boolean verify(List<String> arguments) {
        UniformSuggestionStack stack = this.suggest();
        String multilevel = String.join(" ", arguments.subList(0, Math.min(stack.lengthMultilevel(), arguments.size())))
                .toLowerCase();

        if (multilevel.isEmpty()) {
            return true;
        }

        if (arguments.size() > stack.lengthMultilevel()) {
            for (String suggest : stack.multilevelSuggestions()) {
                if (suggest.toLowerCase().equals(multilevel)) {
                    return true;
                }
            }

            return false;
        }

        for (String suggest : stack.multilevelSuggestions()) {
            if (suggest.toLowerCase().startsWith(multilevel)) {
                return true;
            }
        }

        return false;
    }

}
