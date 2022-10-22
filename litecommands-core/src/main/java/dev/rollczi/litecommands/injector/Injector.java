package dev.rollczi.litecommands.injector;

import java.lang.reflect.Method;

public interface Injector<CONTEXT> {

    <T> T createInstance(Class<T> type, InvokeContext<CONTEXT> context);

    <T> T createInstance(Class<T> type);

    Object invokeMethod(Method method, Object instance, InvokeContext<CONTEXT> context);

    Object invokeMethod(Method method, Object instance);

    InjectorSettings<CONTEXT> settings();

}
