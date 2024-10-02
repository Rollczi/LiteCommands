package dev.rollczi.litecommands.argument.profile;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ArgumentProfile<T extends ArgumentProfile<T>> {

    @ApiStatus.Experimental
    ArgumentProfileNamespace<T> getNamespace();

}
