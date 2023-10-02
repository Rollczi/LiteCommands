package dev.rollczi.litecommands.handler.result;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.HashMap;
import java.util.Map;

public class ResultHandleServiceImpl<SENDER> implements ResultHandleService<SENDER> {

    private final Map<Class<?>, ResultHandler<SENDER, ?>> handlers = new HashMap<>();

    @Override
    public <T> void registerHandler(Class<T> resultType, ResultHandler<SENDER, ? extends T> handler) {
        this.handlers.put(resultType, handler);
    }

    @Override
    public <T> void resolve(Invocation<SENDER> invocation, T result) {
        ResultHandlerChain<SENDER> chain = new ResultHandlerChainImpl<>(this);
        this.resolve(invocation, result, chain);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void resolve(Invocation<SENDER> invocation, T result, ResultHandlerChain<SENDER> chain) {
        Class<T> type = (Class<T>) result.getClass();
        ResultHandler<SENDER, T> handler = this.getHandler(type);

        if (handler == null) {
            throw new LiteCommandsException("Cannot find handler for result type " + type.getName());
        }

        handler.handle(invocation, result, chain);
    }

    @SuppressWarnings("unchecked")
    private <T> ResultHandler<SENDER, T> getHandler(Class<T> resultType) {
        ResultHandler<SENDER, T> handler = (ResultHandler<SENDER, T>) this.handlers.get(resultType);

        if (handler != null) {
            return handler;
        }

        for (Map.Entry<Class<?>, ResultHandler<SENDER, ?>> entry : this.handlers.entrySet()) {
            Class<?> key = entry.getKey();

            if (key.isAssignableFrom(resultType)) {
                handler = (ResultHandler<SENDER, T>) entry.getValue();
                break;
            }
        }

        return handler;
    }

}
