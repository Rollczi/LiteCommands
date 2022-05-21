package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.ArgumentAnnotation;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.handle.LiteException;
import org.panda_lang.utilities.inject.Property;
import panda.std.Option;
import panda.std.Result;
import panda.std.function.TriFunction;
import panda.std.stream.PandaStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

class InjectorHandler<SENDER, A> implements TriFunction<Property, A, Object[], Object> {

    private final Map<Class<?>, Supplier<?>> typeBind = new HashMap<>();
    private final Map<Class<?>, Contextual<SENDER, ?>> contextualBind = new HashMap<>();

    InjectorHandler(Map<Class<?>, Supplier<?>> typeBind, Map<Class<?>, Contextual<SENDER, ?>> contextualBind) {
        this.typeBind.putAll(typeBind);
        this.contextualBind.putAll(contextualBind);
    }

    @Override
    public Object apply(Property property, A second, Object[] args) {
        Option<Parameter> parameterOption = property.getParameter();

        if (parameterOption.isEmpty()) {
            return null;
        }

        Parameter parameter = parameterOption.get();
        Executable executable = parameter.getDeclaringExecutable();

        if (!(executable instanceof Method)) {
            return this.handleTypeBind(property.getType());
        }

        Option<?> optionParm = PandaStream.of(parameter.getAnnotations())
                .map(Annotation::annotationType)
                .find(type -> type.isAnnotationPresent(ArgumentAnnotation.class));

        if (optionParm.isEmpty()) {
            return handleNoArgumentProperty(property, args);
        }

        Method method = (Method) executable;

        int index = 0;
        for (Parameter methodParameter : method.getParameters()) {
            Option<?> option = PandaStream.of(methodParameter.getAnnotations())
                    .map(Annotation::annotationType)
                    .find(type -> type.isAnnotationPresent(ArgumentAnnotation.class));

            if (option.isEmpty()) {
                continue;
            }

            if (methodParameter.equals(parameter)) {
                Object toInject = InvokeContext.fromArgs(args).getParameter(index);

                if (parameter.getType().isAssignableFrom(toInject.getClass())) {
                    return toInject;
                }

                throw new IllegalArgumentException("Cannot inject " + toInject.getClass() + " to " + parameter.getType());
            }

            index++;
        }

        return null;
    }

    private Object handleNoArgumentProperty(Property property, Object[] args) {
        Class<?> type = property.getType();
        Contextual<SENDER, ?> contextual = contextualBind.get(type);

        if (contextual != null) {
            return this.hande(contextual, args);
        }

        for (Map.Entry<Class<?>, Contextual<SENDER, ?>> entry : contextualBind.entrySet()) {
            if (!entry.getKey().isAssignableFrom(type)) {
                continue;
            }

            return this.hande(entry.getValue(), args);
        }

        return this.handleTypeBind(type);
    }

    private Object handleTypeBind(Class<?> type) {
        Supplier<?> supplier = typeBind.get(type);

        if (supplier != null) {
            return supplier.get();
        }

        for (Map.Entry<Class<?>, Supplier<?>> entry : typeBind.entrySet()) {
            if (!entry.getKey().isAssignableFrom(type)) {
                continue;
            }

            return entry.getValue().get();
        }

        throw new IllegalStateException("Can't find type bind or contextual bind for type: " + type);
    }

    public Object hande(Contextual<SENDER, ?> contextual, Object[] args) {
        InvokeContext invokeContext = InvokeContext.fromArgs(args);
        LiteInvocation invocation = invokeContext.getInvocation();
        SENDER sender = (SENDER) invocation.sender().getHandle();

        Result<?, Object> result = contextual.extract(sender, invocation);

        if (result.isErr()) {
            throw new LiteException(result.getError());
        }

        return result.get();
    }

}
