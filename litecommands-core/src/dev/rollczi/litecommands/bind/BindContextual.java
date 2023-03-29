package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.invocation.Invocation;
import panda.std.Result;

public interface BindContextual<SENDER, T> {

    Result<T, Object> extract(Invocation<SENDER> invocation);

}
