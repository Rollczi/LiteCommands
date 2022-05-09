package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.LiteException;
import org.panda_lang.utilities.inject.Injector;

import java.lang.reflect.Method;
import java.util.List;

class MethodExecutor {

    private final Object instance;
    private final Method method;
    private final Injector injector;

    MethodExecutor(Object instance, Method method, Injector injector) {
        this.instance = instance;
        this.method = method;
        this.injector = injector;
    }

    Object execute(LiteInvocation invocation, List<Object> args) {
        try {
            return injector.invokeMethod(method, instance, new InvokeContext(invocation, args));
        }
        catch (LiteException exception) {
            return exception.getValue();
        }
        catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

}
