package dev.rollczi.litecommands.modern.suggestion;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.suggestion.Suggestion;

import java.util.List;

public interface SuggestionResolver<SENDER, DETERMINANT> {

    List<Suggestion> suggestion(Invocation<SENDER> invocation, DETERMINANT context);

    default boolean validate(Invocation<SENDER> invocation, DETERMINANT context) {
        return false;
    }

}
