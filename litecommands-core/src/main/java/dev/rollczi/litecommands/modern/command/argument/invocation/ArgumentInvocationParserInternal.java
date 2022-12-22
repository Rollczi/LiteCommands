package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;

import java.util.List;

public interface ArgumentInvocationParserInternal<SENDER, DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> {

    ArgumentResult<EXPECTED> parse(Invocation<SENDER> invocation, CONTEXT context, List<String> arguments);

    CountRange getArgumentsRange();

}
