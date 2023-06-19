package dev.rollczi.litecommands.command.requirement;

import dev.rollczi.litecommands.argument.Argument;

public interface ArgumentRequirement<SENDER, OUT> extends Requirement<SENDER, OUT> {

    Argument<?> getArgument();

    boolean isOptional();

}
