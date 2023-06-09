package dev.rollczi.litecommands.result;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationResult;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;

public class ResultService<SENDER> {

    private final Map<Class<?>, ResultHandler<SENDER, ?>> handlers = new HashMap<>();
    private final Map<Class<?>, ResultMapper<SENDER, ?, ?>> mappers = new HashMap<>();

    public <T> void registerHandler(Class<T> resultType, ResultHandler<SENDER, ? extends T> handler) {
        this.handlers.put(resultType, handler);
    }

    public <T> void registerMapper(Class<T> resultType, ResultMapper<SENDER, T, ?> mapper) {
        this.mappers.put(resultType, mapper);
    }

    public void resolveInvocation(InvocationResult<SENDER> invocationResult) {
        Invocation<SENDER> invocation = invocationResult.getInvocation();

        if (invocationResult.isInvoked()) {
            this.resolveCommandExecuteResult(invocation, invocationResult.getCommandExecuteResult());
            return;
        }

        this.resolveFailedReason(invocation, invocationResult.getFailedReason());
    }

    public void resolveCommandExecuteResult(Invocation<SENDER> invocation, CommandExecuteResult result) {
        if (result.isSuccessful()) {
            Option<Object> resultResult = result.getResult();

            if (resultResult.isPresent()) {
                this.resolveObject(invocation, resultResult.get());
            }

            return;
        }

        Exception exception = result.getException();

        this.resolveException(invocation, exception);
    }

    public void resolveFailedReason(Invocation<SENDER> invocation, FailedReason failedReason) {
        if (failedReason.isEmpty()) {
            return;
        }

        Object reason = failedReason.getReason();

        this.resolveObject(invocation, reason);
    }

    @SuppressWarnings("unchecked")
    public <T> void resolveObject(Invocation<SENDER> invocation, T result) {
        Class<T> type = (Class<T>) result.getClass();
        ResultHandler<SENDER, T> handler = this.getHandler(type);

        if (handler == null) {
            throw new ResultHandleException("Cannot find handler for result type " + type.getName());
        }

        handler.handle(invocation, result);
    }

    @SuppressWarnings("unchecked")
    public <E extends Exception> void resolveException(Invocation<SENDER> invocation, E exception) {
        Class<E> exceptionClass = (Class<E>) exception.getClass();
        ResultHandler<SENDER, E> handler = this.getHandler(exceptionClass);

        if (handler == null) {
            throw new ResultHandleException("Unhandled exception", exception);
        }

        handler.handle(invocation, exception);
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

                this.resolveObject(invocation, mapped);
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
