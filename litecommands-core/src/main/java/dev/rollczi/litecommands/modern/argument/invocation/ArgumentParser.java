package dev.rollczi.litecommands.modern.argument.invocation;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.argument.ArgumentContext;

import java.util.List;

public interface ArgumentParser<SENDER, DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> {

    ArgumentResult<EXPECTED> parse(Invocation<SENDER> invocation, CONTEXT contextBox, List<String> arguments);

    CountRange getArgumentCount();

}
