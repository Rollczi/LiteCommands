package dev.rollczi.litecommands.command.sugesstion;

import panda.utilities.text.Joiner;

import java.util.List;

public interface Suggester {

    Suggester NONE = TwinSuggestionStack::empty;

    static Suggester of(Iterable<Suggestion> iterable) {
        return new IteratorSuggester(iterable);
    }

    TwinSuggestionStack suggest();

    default boolean verify(List<String> arguments) {
        TwinSuggestionStack stack = this.suggest();
        String multilevel = Joiner.on(" ")
                .join(arguments.subList(0, Math.min(stack.multilevelLength(), arguments.size())))
                .toString()
                .toLowerCase();

        if (multilevel.isEmpty()) {
            return true;
        }

        if (arguments.size() > stack.multilevelLength()) {
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
