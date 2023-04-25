package dev.rollczi.litecommands.command.requirements;

import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.argument.input.InputArgumentsMatcher;
import dev.rollczi.litecommands.invocation.Invocation;

public interface CommandRequirement<SENDER, RESULT> {

    <CONTEXT extends InputArgumentsMatcher<CONTEXT>> CommandRequirementResult<RESULT> check(Invocation<SENDER> invocation, InputArguments<CONTEXT> inputArguments, CONTEXT context);

}
