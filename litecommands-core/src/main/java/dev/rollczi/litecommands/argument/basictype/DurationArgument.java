package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Blank;
import panda.std.Option;
import panda.std.Result;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class DurationArgument implements OneArgument<Duration> {

    @Override
    public Result<Duration, ?> parse(LiteInvocation invocation, String argument) {
        return this.parseDuration(argument)
                .toResult(Blank.BLANK);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return TypeUtils.suggestion(invocation, TypeUtils.DURATION_SUGGESTION);
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return this.parseDuration(suggestion.single()).isPresent();
    }

    private Option<Duration> parseDuration(String arg) {
        return Option.supplyThrowing(DateTimeParseException.class, () -> {
            String upperCase = arg.toUpperCase(Locale.ROOT);

            if (upperCase.contains("D")) {
                String replaced = "P" + (upperCase.endsWith("D")
                    ? upperCase
                    : upperCase.replace("D", "DT"));
                return Duration.parse(replaced);
            }

            return Duration.parse("PT" + upperCase);
        });
    }

}
