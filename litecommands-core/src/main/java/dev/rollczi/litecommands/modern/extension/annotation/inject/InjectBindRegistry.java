package dev.rollczi.litecommands.modern.extension.annotation.inject;

import dev.rollczi.litecommands.modern.command.Invocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class InjectBindRegistry<SENDER> {

    private final Map<Class<?>, Supplier<?>> instanceBindings = new HashMap<>();
    private final Map<Class<?>, Function<Invocation<SENDER>, ?>> contextualBindings = new HashMap<>();

    public <T> void bindInstance(Class<T> clazz, Supplier<T> supplier) {
        instanceBindings.put(clazz, supplier);
    }

    public <T> void bindContextual(Class<T> clazz, Function<Invocation<SENDER>, T> supplier) {
        contextualBindings.put(clazz, supplier);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz, Invocation<SENDER> invocation) {
        T instance = this.getInstance(clazz);

        if (instance != null) {
            return instance;
        }

        Function<Invocation<SENDER>, T> contextualSupplier = (Function<Invocation<SENDER>, T>) contextualBindings.get(clazz);

        if (contextualSupplier != null) {
            return contextualSupplier.apply(invocation);
        }

        for (Map.Entry<Class<?>, Function<Invocation<SENDER>, ?>> entry : contextualBindings.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) {
                return ((Function<Invocation<SENDER>, T>) entry.getValue()).apply(invocation);
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz) {
        Supplier<T> supplier = (Supplier<T>) instanceBindings.get(clazz);

        if (supplier != null) {
            return supplier.get();
        }

        for (Map.Entry<Class<?>, Supplier<?>> entry : instanceBindings.entrySet()) {
            if (clazz.isAssignableFrom(entry.getKey())) {
                return (T) entry.getValue().get();
            }
        }

        return null;
    }

}
