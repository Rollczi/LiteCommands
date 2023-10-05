package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.invocation.Invocation;

@FunctionalInterface
public interface InvokedMessage<SENDER, T, CONTEXT> {

    T get(Invocation<SENDER> invocation, CONTEXT context);

}
