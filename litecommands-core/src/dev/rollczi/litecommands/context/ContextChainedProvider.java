package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ContextChainedProvider<SENDER, T> {

    ContextResult<T> provide(Invocation<SENDER> invocation, ContextChainAccessor<SENDER> accessor);

}
