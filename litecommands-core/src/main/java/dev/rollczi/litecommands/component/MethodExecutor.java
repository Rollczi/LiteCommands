package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.annotations.Handler;
import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import dev.rollczi.litecommands.argument.ArgumentHandler;
import dev.rollczi.litecommands.utils.InjectUtils;
import dev.rollczi.litecommands.utils.ReflectUtils;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import org.panda_lang.utilities.inject.Injector;
import panda.std.Option;
import panda.std.Pair;
import panda.std.Result;
import panda.std.stream.PandaStream;
import panda.utilities.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public final class MethodExecutor {

    private final Method method;
    private final Object instance;
    private final Injector injector;
    private final Map<Integer, ArgumentHandler<?>> cachedArguments;

    public MethodExecutor(Method method, Object instance, Injector injector, AnnotationParser annotationParser) {
        this.method = method;
        this.instance = instance;
        this.injector = injector;
        this.cachedArguments = PandaStream.of(method.getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(Arg.class))
                .toMap(parameter -> parameter.getAnnotation(Arg.class).value(), parameter -> {
                    Handler handler = parameter.getAnnotation(Handler.class);

                    return PandaStream.of(annotationParser.getArgumentHandler(parameter.getType()))
                            .filter(argumentHandler -> !parameter.isAnnotationPresent(Handler.class) || handler.value().equals(argumentHandler.getNativeClass()))
                            .any()
                            .orThrow(() -> new RuntimeException("Can't index parameters of " + ReflectUtils.formatMethodParams(method)));
                });
    }

    public Option<ArgumentHandler<?>> getArgumentHandler(int currentArgsCount) {
        return Option.of(cachedArguments.get(currentArgsCount));
    }

    public Map<Integer, ArgumentHandler<?>> getArgumentHandlers() {
        return Collections.unmodifiableMap(cachedArguments);
    }

    public Result<Option<Object>, Pair<String, Throwable>> execute(LiteComponent.ContextOfResolving context) {
        try {
            Object returned = injector.invokeMethod(method, instance, InjectUtils.prepareInjectorArgs(context, this));
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
