package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.inject.InjectContext;
import dev.rollczi.litecommands.utils.ReflectUtils;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import org.panda_lang.utilities.inject.Injector;
import panda.std.Option;
import panda.std.Pair;
import panda.std.Result;
import panda.std.stream.PandaStream;
import panda.utilities.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public final class MethodExecutor {

    private final Method method;
    private final Object instance;
    private final Injector injector;
    private final  Map<Integer, Class<?>> cachedParameters;

    public MethodExecutor(Method method, Object instance, Injector injector) {
        this.method = method;
        this.instance = instance;
        this.injector = injector;
        this.cachedParameters = PandaStream.of(method.getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(Arg.class))
                .toMap(parameter -> parameter.getAnnotation(Arg.class).value(), Parameter::getType);
    }

    public Option<Class<?>> getParameter(int currentArgsCount) {
        return Option.of(cachedParameters.get(currentArgsCount));
    }

    public Result<Option<Object>, Pair<String, Throwable>> execute(InjectContext context) {
        try {
            Object returned = injector.invokeMethod(method, instance, context);
            return Result.ok(Option.of(returned));
        } catch (IllegalAccessException illegalAccessException) {
            return Result.error(Pair.of("Method " + ReflectUtils.formatMethodParams(method) + " is " + ReflectUtils.modifier(method), illegalAccessException));
        } catch (IllegalArgumentException illegalArgumentException) {
            return Result.error(Pair.of("Illegal arguments for method " + ReflectUtils.formatMethodParams(method), illegalArgumentException));
        } catch (ValidationCommandException exception) {
            return Result.error(Pair.of(StringUtils.EMPTY, exception));
        } catch (Throwable throwable) {
            Throwable cause = throwable.getCause();

            if (cause instanceof ValidationCommandException) {
                return Result.error(Pair.of(StringUtils.EMPTY, cause));
            }

            return Result.error(Pair.of("Can't invoke method " + ReflectUtils.formatMethodParams(method), throwable));
        }
    }

}
