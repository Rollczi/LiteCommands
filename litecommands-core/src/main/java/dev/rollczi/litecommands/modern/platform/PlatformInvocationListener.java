package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.invocation.InvocationResult;

@FunctionalInterface
public interface PlatformInvocationListener<SENDER> {

    InvocationResult<SENDER> execute(Invocation<SENDER> invocation);

}
