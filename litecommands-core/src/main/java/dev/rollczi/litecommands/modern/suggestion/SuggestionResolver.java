package dev.rollczi.litecommands.modern.suggestion;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.count.WithCountRange;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggestion;

import java.util.List;

public interface SuggestionResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> extends WithCountRange {

    List<Suggestion> suggestion(Invocation<SENDER> invocation, ARGUMENT context, Suggestion suggestion);

    default boolean validate(Invocation<SENDER> invocation, ARGUMENT context) {
        return false;
    }

}
