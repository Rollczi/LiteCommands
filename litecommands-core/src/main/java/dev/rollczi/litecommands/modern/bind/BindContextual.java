package dev.rollczi.litecommands.modern.bind;

import dev.rollczi.litecommands.modern.invocation.Invocation;
import panda.std.Result;

public interface BindContextual<SENDER, T> {

    Result<T, Object> extract(Invocation<SENDER> invocation);

}
