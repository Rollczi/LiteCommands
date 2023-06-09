package dev.rollczi.litecommands.result;

import dev.rollczi.litecommands.invocation.Invocation;

public interface ResultMapper<SENDER, FROM, TO> {

    TO map(Invocation<SENDER> invocation, FROM from);

}
