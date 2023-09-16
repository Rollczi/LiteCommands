package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.requirement.Requirement;

public interface ArgumentRequirement<SENDER, PARSED> extends Requirement<SENDER, PARSED> {

    Argument<PARSED> getArgument();

    boolean isWrapperOptional();

}
