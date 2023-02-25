package dev.rollczi.litecommands.modern.argument;

import dev.rollczi.litecommands.modern.count.WithCountRange;
import dev.rollczi.litecommands.modern.invocation.Invocation;

import java.util.List;

public interface ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> extends WithCountRange {

    ArgumentResult<EXPECTED> parse(Invocation<SENDER> invocation, List<String> arguments, ARGUMENT context);

}
