package dev.rollczi.litecommands.priority;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface Prioritized {

    @ApiStatus.Experimental
    Priority getPriority();

}
