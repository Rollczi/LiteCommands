package dev.rollczi.litecommands.modern.bind;

import dev.rollczi.litecommands.modern.exception.LiteException;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.util.MapUtil;
import panda.std.Option;
import panda.std.Result;

import java.util.HashMap;
import java.util.Map;
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
    public <T> Result<T, Object> getInstance(Class<T> clazz, Invocation<SENDER> invocation) {
        Result<T, Object> result = this.getInstance(clazz);

        if (result.isOk()) {
            return result;
        }

        BindContextual<SENDER, T> contextualSupplier = (BindContextual<SENDER, T>) this.contextualBindings.get(clazz);

        if (contextualSupplier != null) {
            Result<T, Object> extract = contextualSupplier.extract(invocation);

            if (extract.isErr()) {
                throw new LiteException(extract.getError());
            }

            return extract;
        }

        for (Map.Entry<Class<?>, BindContextual<SENDER, ?>> entry : this.contextualBindings.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) {
                BindContextual<SENDER, T> bindContextual = (BindContextual<SENDER, T>) entry.getValue();

                return bindContextual.extract(invocation);
            }
        }

        return Result.error("Cannot find binding for " + clazz.getName());
    }

    @SuppressWarnings("unchecked")
    public <T> Result<T, Object> getInstance(Class<T> clazz) {
        Option<Supplier<?>> option = MapUtil.findKeyInstanceOf(clazz, this.instanceBindings);

        if (option.isPresent()) {
            Supplier<T> supplier = (Supplier<T>) option.get();

            return Result.ok(supplier.get());
        }

        return Result.error("Cannot find binding for " + clazz.getName());
    }

}
