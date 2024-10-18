package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface MutableArgument<T> extends Argument<T> {

    @ApiStatus.Experimental
    @ApiStatus.Internal
    <P extends ArgumentProfile<P>> Argument<T> addProfile(P profile);

    @ApiStatus.Internal
    void setKey(ArgumentKey key);

}
