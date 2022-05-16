package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.handle.LiteException;
import org.panda_lang.utilities.inject.Property;
import panda.std.Result;
import panda.std.function.TriFunction;

class ContextualHandler<SENDER, T, A> implements TriFunction<Property, A, Object[], Object> {

    private final Contextual<SENDER, T> contextual;

    ContextualHandler(Contextual<SENDER, T> contextual) {
        this.contextual = contextual;
    }

    @Override
    public Object apply(Property first, A second, Object[] args) {
        InvokeContext invokeContext = InvokeContext.fromArgs(args);
        LiteInvocation invocation = invokeContext.getInvocation();
        SENDER sender = (SENDER) invocation.sender().getHandle();

        Result<?, Object> result = contextual.extract(sender, invocation);

        if (result.isErr()) {
            throw new LiteException(result.getError());
        }

        return result.get();
    }

}
