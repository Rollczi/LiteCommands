package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;

public class CommandExecuteResultResolver<SENDER> {

    private final Map<Class<?>, CommandExecuteResultHandler<SENDER, ?>> handlers = new HashMap<>();
    private final Map<Class<?>, CommandExecuteResultMapper<SENDER, ?, ?>> mappers = new HashMap<>();

    public void registerHandler(Class<?> resultType, CommandExecuteResultHandler<SENDER, ?> handler) {
        handlers.put(resultType, handler);
    }

    public void registerMapper(Class<?> resultType, CommandExecuteResultMapper<SENDER, ?, ?> mapper) {
        mappers.put(resultType, mapper);
    }

    public void resolve(Invocation<SENDER> invocation, CommandExecuteResult result) {
        if (result.isSuccessful()) {
            Option<Object> resultResult = result.getResult();

            if (resultResult.isPresent()) {
                resolveObjectResult(invocation, resultResult.get());
            }

            return;
        }

        Exception exception = result.getException();

        resolveFailed(invocation, exception);
    }

    public void resolveFailedReason(Invocation<SENDER> invocation, FailedReason failedReason) {
        if (failedReason.isEmpty()) {
            return;
        }

        Object reason = failedReason.getReason();

        resolveObjectResult(invocation, reason);
    }

    @SuppressWarnings("unchecked")
    private <T> void resolveObjectResult(Invocation<SENDER> invocation, T result) {
        Class<T> type = (Class<T>) result.getClass();
        CommandExecuteResultHandler<SENDER, T> handler = getHandler(type);

        if (handler == null) {
            throw new CommandExecuteException("Cannot find handler for result type " + type.getName());
        }

        handler.handle(invocation, result);
    }

    @SuppressWarnings("unchecked")
    private <E extends Exception> void resolveFailed(Invocation<SENDER> invocation, E exception) {
        Class<E> exceptionClass = (Class<E>) exception.getClass();
        CommandExecuteResultHandler<SENDER, E> handler = this.getHandler(exceptionClass);

        if (handler == null) {
            throw new CommandExecuteException("Unhandled exception", exception);
        }

        handler.handle(invocation, exception);
    }

    @SuppressWarnings("unchecked")
    private <T> CommandExecuteResultHandler<SENDER, T> getHandler(Class<T> resultType) {
        CommandExecuteResultHandler<SENDER, T> handler = (CommandExecuteResultHandler<SENDER, T>) handlers.get(resultType);

        if (handler != null) {
            return handler;
        }

        for (Map.Entry<Class<?>, CommandExecuteResultHandler<SENDER, ?>> entry : handlers.entrySet()) {
            Class<?> key = entry.getKey();

            if (key.isAssignableFrom(resultType)) {
                handler = (CommandExecuteResultHandler<SENDER, T>) entry.getValue();
                break;
            }
        }

        if (handler != null) {
            return handler;
        }

        CommandExecuteResultMapper<SENDER, T, ?> mapper = getMapper(resultType);

        if (mapper != null) {
            return (invocation, result) -> {
                Object mapped = mapper.map(invocation, result);

                resolveObjectResult(invocation, mapped);
            };
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> CommandExecuteResultMapper<SENDER, T, ?> getMapper(Class<T> resultType) {
        CommandExecuteResultMapper<SENDER, T, ?> mapper = (CommandExecuteResultMapper<SENDER, T, ?>) mappers.get(resultType);

        if (mapper != null) {
            return mapper;
        }

        for (Map.Entry<Class<?>, CommandExecuteResultMapper<SENDER, ?, ?>> entry : mappers.entrySet()) {
            Class<?> key = entry.getKey();

            if (key.isAssignableFrom(resultType)) {
                mapper = (CommandExecuteResultMapper<SENDER, T, ?>) entry.getValue();
                break;
            }
        }

        return mapper;
    }

}
