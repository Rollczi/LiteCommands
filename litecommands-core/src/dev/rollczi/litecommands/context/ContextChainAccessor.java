package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ContextChainAccessor<SENDER>  {

    <T> ContextResult<T> provideContext(Class<T> clazz, Invocation<SENDER> invocation);

}
