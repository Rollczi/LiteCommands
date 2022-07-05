package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.handle.LiteException;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.injector.InvokeContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MethodExecutor<SENDER> {

    private final Object instance;
    private final Method method;
    private final Injector<SENDER> injector;

    MethodExecutor(Object instance, Method method, Injector<SENDER> injector) {
        this.instance = instance;
        this.method = method;
        this.injector = injector;
    }

    Object execute(Invocation<SENDER> invocation, List<Object> args) {
        try {
            return injector.invokeMethod(method, instance, new InvokeContext<>(invocation, args));
        }
        catch (LiteException exception) {
            return exception.getResult();
        }
        catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

}
