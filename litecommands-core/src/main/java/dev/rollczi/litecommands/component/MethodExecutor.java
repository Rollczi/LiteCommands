package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.annotations.Handler;
import dev.rollczi.litecommands.annotations.Name;
import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import dev.rollczi.litecommands.argument.ArgumentHandler;
import dev.rollczi.litecommands.utils.InjectUtils;
import dev.rollczi.litecommands.utils.ReflectUtils;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import org.jetbrains.annotations.NotNull;
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
    private final Map<Integer, ParameterHandler<?>> cachedParameterHandlers;

    public MethodExecutor(Method method, Object instance, Injector injector, AnnotationParser annotationParser) {
        this.method = method;
        this.instance = instance;
        this.injector = injector;
        this.cachedParameterHandlers = PandaStream.of(method.getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(Arg.class))
                .toMap(parameter -> parameter.getAnnotation(Arg.class).value(), parameter -> {
                    Arg argAnnotation = parameter.getAnnotation(Arg.class);
                    Handler handlerAnnotation = parameter.getAnnotation(Handler.class);
                    Name nameAnnotation = parameter.getAnnotation(Name.class);

                    Option<ArgumentHandler<?>> option = PandaStream.of(annotationParser.getArgumentHandlers(parameter.getType()))
                            .filter(argumentHandler -> handlerAnnotation == null || handlerAnnotation.value().equals(argumentHandler.getNativeClass()))
                            .any();

                    if (option.isEmpty()) {
                        throw new RuntimeException("Can't index argument " + ReflectUtils.formatClass(parameter.getType()) + " on " + ReflectUtils.formatMethodParams(method));
                    }

                    ArgumentHandler<?> argumentHandler = option.get();
                    String name = nameAnnotation != null
                            ? nameAnnotation.value()
                            : !argAnnotation.name().isEmpty()
                            ? argAnnotation.name()
                            : argumentHandler.getName();

                    return new ParameterHandler<>(name, argumentHandler);
                });
    }

    public Option<ParameterHandler<?>> getParameterHandlers(int currentArgsCount) {
        return Option.of(cachedParameterHandlers.get(currentArgsCount));
    }

    public Map<Integer, ParameterHandler<?>> getParameterHandlers() {
        return Collections.unmodifiableMap(cachedParameterHandlers);
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
