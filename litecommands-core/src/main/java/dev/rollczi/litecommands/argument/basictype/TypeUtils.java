package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Blank;
import panda.std.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

final class TypeUtils {

    static final String[] NUMBER_SUGGESTION = { "0", "1", "5", "10", "50", "100", "500" };
    static final String[] NUMBER_SHORT_SUGGESTION = { "0", "1", "5", "10", "50" };
    static final String[] STRING_SUGGESTION = { "text" };
    static final String[] BOOLEAN_SUGGESTION = { "true", "false" };
    static final String[] DECIMAL_SUGGESTION = { "0", "1", "1.5", "10", "10.5", "100", "100.5" };
    static final String[] CHARACTER_SUGGESTION = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    private TypeUtils() {}

    static <T> Result<T, Blank> parse(Supplier<T> parse) {
        return Result.supplyThrowing(NumberFormatException.class, parse::get).mapErrToBlank();
    }

    static List<Suggestion> suggestion(LiteInvocation invocation, String... suggestions) {
        List<Suggestion> parsedSuggestions = new ArrayList<>(Suggestion.of(suggestions));
        Optional<Suggestion> optionalSuggestion = invocation.argument(invocation.arguments().length - 1)
                .filter(argument -> !argument.isEmpty())
                .map(Suggestion::of);

        optionalSuggestion.ifPresent(parsedSuggestions::add);

        return parsedSuggestions;
    }

    static boolean validate(Function<String, ?> parse, Suggestion suggestion) {
        try {
            parse.apply(suggestion.single());
            return true;
        }
        catch (NumberFormatException ignore) {
            return false;
        }
    }

}
