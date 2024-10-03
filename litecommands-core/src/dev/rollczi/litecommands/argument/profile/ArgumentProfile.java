package dev.rollczi.litecommands.argument.profile;

import dev.rollczi.litecommands.priority.Prioritized;
import dev.rollczi.litecommands.priority.PriorityLevel;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ArgumentProfile<T extends ArgumentProfile<T>> extends Prioritized {

    @ApiStatus.Experimental
    ArgumentProfileNamespace<T> getNamespace();

    @Override
    default PriorityLevel getPriority() {
        return PriorityLevel.NORMAL;
    }

}
