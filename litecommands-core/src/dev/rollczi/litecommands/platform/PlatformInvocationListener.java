package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.argument.input.ArgumentsInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationResult;

@FunctionalInterface
public interface PlatformInvocationListener<SENDER> {

    InvocationResult<SENDER> execute(Invocation<SENDER> invocation, ArgumentsInput<?> arguments);

}
