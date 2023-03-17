package dev.rollczi.litecommands.modern.bind;

import dev.rollczi.litecommands.modern.exception.LiteException;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import panda.std.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class BindRegistry<SENDER> {

    private final Map<Class<?>, Supplier<?>> instanceBindings = new HashMap<>();
    private final Map<Class<?>, BindContextual<SENDER, ?>> contextualBindings = new HashMap<>();

    public <T> void bindInstance(Class<T> on, Supplier<T> bind) {
        this.instanceBindings.put(on, bind);
    }

    public void bindInstanceUnsafe(Class<?> on, Supplier<?> bind) {
        this.instanceBindings.put(on, bind);
    }

    public <T> void bindContextual(Class<T> clazz, BindContextual<SENDER, T> supplier) {
        this.contextualBindings.put(clazz, supplier);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz, Invocation<SENDER> invocation) {
        T instance = this.getInstance(clazz);

        if (instance != null) {
            return instance;
        }

        BindContextual<SENDER, T> contextualSupplier = (BindContextual<SENDER, T>) this.contextualBindings.get(clazz);

        if (contextualSupplier != null) {
            Result<T, Object> extract = contextualSupplier.extract(invocation);

            if (extract.isErr()) {
                throw new LiteException(extract.getError());
            }

            return extract.get();
        }

        for (Map.Entry<Class<?>, BindContextual<SENDER, ?>> entry : this.contextualBindings.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) {
                return ((Function<Invocation<SENDER>, T>) entry.getValue()).apply(invocation);
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz) {
        Supplier<T> supplier = (Supplier<T>) this.instanceBindings.get(clazz);

        if (supplier != null) {
            return supplier.get();
        }

        for (Map.Entry<Class<?>, Supplier<?>> entry : this.instanceBindings.entrySet()) {
            if (clazz.isAssignableFrom(entry.getKey())) {
                return (T) entry.getValue().get();
            }
        }

        return null;
    }

}
