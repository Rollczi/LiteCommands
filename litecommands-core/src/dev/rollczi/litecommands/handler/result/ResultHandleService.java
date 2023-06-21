package dev.rollczi.litecommands.handler.result;

import dev.rollczi.litecommands.exception.LiteCommandsException;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.HashMap;
import java.util.Map;

public class ResultHandleService<SENDER> {

    private final Map<Class<?>, ResultHandler<SENDER, ?>> handlers = new HashMap<>();
    private final Map<Class<?>, ResultMapper<SENDER, ?, ?>> mappers = new HashMap<>();

    public <T> void registerHandler(Class<T> resultType, ResultHandler<SENDER, ? extends T> handler) {
        this.handlers.put(resultType, handler);
    }

    public <T> void registerMapper(Class<T> resultType, ResultMapper<SENDER, T, ?> mapper) {
        this.mappers.put(resultType, mapper);
    }

    @SuppressWarnings("unchecked")
    public <T> void resolve(Invocation<SENDER> invocation, T result) {
        Class<T> type = (Class<T>) result.getClass();
        ResultHandler<SENDER, T> handler = this.getHandler(type);

        if (handler == null) {
            throw new LiteCommandsException("Cannot find handler for result type " + type.getName());
        }

        handler.handle(invocation, result);
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

        if (handler != null) {
            return handler;
        }

        ResultMapper<SENDER, T, ?> mapper = this.getMapper(resultType);

        if (mapper != null) {
            return (invocation, result) -> {
                Object mapped = mapper.map(invocation, result);

                this.resolve(invocation, mapped);
            };
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> ResultMapper<SENDER, T, ?> getMapper(Class<T> resultType) {
        ResultMapper<SENDER, T, ?> mapper = (ResultMapper<SENDER, T, ?>) this.mappers.get(resultType);

        if (mapper != null) {
            return mapper;
        }

        for (Map.Entry<Class<?>, ResultMapper<SENDER, ?, ?>> entry : this.mappers.entrySet()) {
            Class<?> key = entry.getKey();

            if (key.isAssignableFrom(resultType)) {
                mapper = (ResultMapper<SENDER, T, ?>) entry.getValue();
                break;
            }
        }

        return mapper;
    }

}
