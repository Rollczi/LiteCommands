package dev.rollczi.litecommands.modern.command.suggestion;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.count.WithCountRange;
import dev.rollczi.litecommands.suggestion.Suggestion;

import java.util.List;

public interface SuggestionResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> extends WithCountRange {

    List<Suggestion> suggestion(Invocation<SENDER> invocation, CONTEXT context);

    default boolean validate(Invocation<SENDER> invocation, CONTEXT context) {
        return false;
    }

}
