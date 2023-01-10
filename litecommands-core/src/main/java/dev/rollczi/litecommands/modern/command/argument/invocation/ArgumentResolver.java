package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.count.WithCountRange;

import java.util.List;

public interface ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> extends WithCountRange {

    ArgumentResult<EXPECTED> parse(Invocation<SENDER> invocation, List<String> arguments, CONTEXT context);

}
