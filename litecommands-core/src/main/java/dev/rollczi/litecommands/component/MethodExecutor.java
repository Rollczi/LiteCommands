package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.inject.InjectContext;
import dev.rollczi.litecommands.utils.ReflectUtils;
import org.panda_lang.utilities.inject.Injector;
import panda.std.Pair;
import panda.std.Result;

import java.lang.reflect.Method;

public final class MethodExecutor {

    private final Method method;
    private final Object instance;
    private final Injector injector;

    public MethodExecutor(Method method, Object instance, Injector injector) {
        this.method = method;
        this.instance = instance;
        this.injector = injector;
    }

    public Result<Object, Pair<String, Throwable>> execute(InjectContext context) {
        try {
            Object returned = injector.invokeMethod(method, instance, context);
            return Result.ok(returned == null ? new Object() : returned);
        } catch (IllegalAccessException illegalAccessException) {
            return Result.error(Pair.of("Method " + ReflectUtils.formatMethodParams(method) + " is " + ReflectUtils.modifier(method), illegalAccessException));
        } catch (IllegalArgumentException illegalArgumentException) {
            return Result.error(Pair.of("Illegal arguments for method " + ReflectUtils.formatMethodParams(method), illegalArgumentException));
        } catch (Throwable throwable) {
            return Result.error(Pair.of("Can't invoke method " + ReflectUtils.formatMethodParams(method), throwable));
        }
    }

}
