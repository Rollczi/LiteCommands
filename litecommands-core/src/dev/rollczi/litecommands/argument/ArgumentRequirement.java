package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.requirement.Requirement;

public interface ArgumentRequirement<SENDER, PARSED> extends Requirement<SENDER, PARSED> {

    Argument<PARSED> getArgument();

    boolean isWrapperOptional();

}
