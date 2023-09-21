package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.invocation.Invocation;
import panda.std.Result;

@Deprecated
public interface LegacyContextProvider<SENDER, T> extends ContextProvider<SENDER, T> {

    Result<T, Object> provideLegacy(Invocation<SENDER> invocation);

    @Override
    default ContextResult<T> provide(Invocation<SENDER> invocation) {
        Result<T, Object> result = provideLegacy(invocation);

        if (result.isErr()) {
            return ContextResult.error(result.getError());
        }

        return ContextResult.ok(() -> result.get());
    }

}
