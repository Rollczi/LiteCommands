package dev.rollczi.litecommands.implementation.injector;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.injector.Injectable;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.injector.InjectorSettings;
import dev.rollczi.litecommands.injector.InvokeContext;
import dev.rollczi.litecommands.shared.ReflectFormat;
import org.jetbrains.annotations.Nullable;
import panda.std.Option;
import panda.std.Result;
import panda.std.function.ThrowingBiFunction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class CommandInjector<SENDER> implements Injector<SENDER> {

    private final InjectorContextProcessor<SENDER> processor;

    public CommandInjector(LiteInjectorSettings<SENDER> processor) {
        this.processor = new InjectorContextProcessor<>(processor.duplicate());
    }

    @Override
    public <T> T createInstance(Class<T> type, InvokeContext<SENDER> context) {
        return this.invokeExecutables(type, type.getDeclaredConstructors(), context, (constructor, arg) -> type.cast(constructor.newInstance(arg)))
                .orThrow(() -> new IllegalStateException("Can't create new instance of class " + ReflectFormat.singleClass(type)));
    }

    @Override
    public <T> T createInstance(Class<T> type) {
        return this.createInstance(type, null);
    }

    @Override
    public Object invokeMethod(Method method, Object instance, InvokeContext<SENDER> context) {
        return this.invokeExecutables(Object.class, new Method[]{ method }, context, (m, arg) -> m.invoke(instance, arg))
                .orThrow(() -> new IllegalStateException("The method " + ReflectFormat.docsMethod(method) + " cannot be invoked!"));
    }

    @Override
    public Object invokeMethod(Method method, Object instance) {
        return this.invokeMethod(method, instance, null);
    }

    @Override
    public InjectorSettings<SENDER> settings() {
        return this.processor.settings().duplicate();
    }

    private <T extends Executable, R> Option<R> invokeExecutables(Class<R> type, T[] executables, @Nullable InvokeContext<SENDER> context, Invoker<T, R> invoker) {
        List<ReflectiveOperationException> errors = new ArrayList<>();

        for (T executable : executables) {
            Result<R, ReflectiveOperationException> result = context == null
                    ? this.invokeExecutable(executable, invoker)
                    : this.invokeExecutable(executable, context, invoker);

            if (result.isOk()) {
                return Option.of(result.get());
            }

            errors.add(result.getError());
        }

        if (!errors.isEmpty()) {
            RuntimeException exception = new RuntimeException();

            for (ReflectiveOperationException error : errors) {
                exception.addSuppressed(error);
            }

            throw exception;
        }

        return Option.none();
    }

    private <T extends Executable, R> Result<R, ReflectiveOperationException> invokeExecutable(T executable, InvokeContext<SENDER> context, Invoker<T, R> invoker) {
        Iterator<Object> iterator = context.getInjectable().iterator();
        Invocation<SENDER> invocation = context.getInvocation();

        List<Object> parameters = new ArrayList<>();
        List<Class<?>> missing = new ArrayList<>();

        for (Parameter parameter : executable.getParameters()) {
            if (this.isInjectAnnotation(parameter)) {
                if (!iterator.hasNext()) {
                    throw new IllegalStateException("Missing arguments for command");
                }

                parameters.add(iterator.next());
                continue;
            }

            Class<?> type = parameter.getType();
            Option<?> option = this.processor.extract(type, invocation);

            if (option.isPresent()) {
                parameters.add(option.get());
                continue;
            }

            missing.add(type);
        }

        if (!missing.isEmpty()) {
            return Result.error(new MissingBindException(missing));
        }

        try {
            executable.setAccessible(true);
            return Result.ok(invoker.apply(executable, parameters.toArray(new Object[0])));
        }
        catch (ReflectiveOperationException exception) {
            return Result.error(exception);
        }
    }

    private <T extends Executable, R> Result<R, ReflectiveOperationException> invokeExecutable(T executable, Invoker<T, R> invoker) {
        List<Object> parameters = new ArrayList<>();
        List<Class<?>> missing = new ArrayList<>();

        for (Parameter parameter : executable.getParameters()) {
            Class<?> type = parameter.getType();
            Option<?> option = this.processor.extract(type);

            if (option.isPresent()) {
                parameters.add(option.get());
                continue;
            }

            missing.add(type);
        }

        if (!missing.isEmpty()) {
            return Result.error(new MissingBindException(missing));
        }

        try {
            executable.setAccessible(true);
            return Result.ok(invoker.apply(executable, parameters.toArray(new Object[0])));
        }
        catch (ReflectiveOperationException exception) {
            return Result.error(exception);
        }
    }

    private boolean isInjectAnnotation(Parameter parameter) {
        for (Annotation annotation : parameter.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(Injectable.class)) {
                return true;
            }
        }

        return false;
    }

    private interface Invoker<T extends Executable, R> extends ThrowingBiFunction<T, Object[], R, ReflectiveOperationException> {
    }

}
