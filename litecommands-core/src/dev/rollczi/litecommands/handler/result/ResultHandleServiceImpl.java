package dev.rollczi.litecommands.handler.result;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.util.MapUtil;

import java.util.HashMap;
import java.util.Map;

public class ResultHandleServiceImpl<SENDER> implements ResultHandleService<SENDER> {

    private final Map<Class<?>, ResultHandler<SENDER, ?>> handlers = new HashMap<>();

    @Override
    public <T> void registerHandler(Class<T> resultType, ResultHandler<SENDER, ? extends T> handler) {
        this.handlers.put(resultType, handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void resolve(Invocation<SENDER> invocation, T result) {
        ResultHandlerChain<SENDER> chain = new ResultHandlerChainImpl<>(this);
        this.resolve(invocation, result, (Class<T>) result.getClass(), chain);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void resolve(Invocation<SENDER> invocation, T result, Class<? super T> typedAs, ResultHandlerChain<SENDER> chain) {
        ResultHandler<SENDER, ? super T> handler = this.getHandler(typedAs);

        if (handler == null) {
            throw new LiteCommandsException("Cannot find handler for result type " + typedAs.getName());
        }

        if (typedAs.isArray() && typedAs.getComponentType().isPrimitive()) {
            result = (T) MapUtil.getBoxedArrayFromPrimitiveArray(result);
        }

        handler.handle(invocation, result, chain);
    }

    @SuppressWarnings("unchecked")
    private <T> ResultHandler<SENDER, T> getHandler(Class<T> resultType) {
        ResultHandler<SENDER, T> handler = (ResultHandler<SENDER, T>) this.handlers.get(resultType);

        if (handler != null) {
            return handler;
        }

        return (ResultHandler<SENDER, T>) MapUtil.findBySuperTypeOf(resultType, this.handlers)
            .orElse(null);
    }

}
