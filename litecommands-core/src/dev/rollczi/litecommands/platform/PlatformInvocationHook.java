package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationResult;

@FunctionalInterface
public interface PlatformInvocationHook<SENDER> {

    InvocationResult<SENDER> execute(Invocation<SENDER> invocation);

}
