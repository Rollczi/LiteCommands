package dev.rollczi.litecommands.modern.suggestion;

import dev.rollczi.litecommands.modern.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.count.WithCountRange;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggestion;

import java.util.List;

public interface SuggestionResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>> extends WithCountRange {

    List<Suggestion> suggestion(Invocation<SENDER> invocation, CONTEXT context, Suggestion suggestion);

    default boolean validate(Invocation<SENDER> invocation, CONTEXT context) {
        return false;
    }

}
