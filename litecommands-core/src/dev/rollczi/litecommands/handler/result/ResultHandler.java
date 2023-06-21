package dev.rollczi.litecommands.handler.result;

import dev.rollczi.litecommands.invocation.Invocation;

public interface ResultHandler<SENDER, T> {

    void handle(Invocation<SENDER> invocation, T result);

}
