package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.util.MapUtil;
import dev.rollczi.litecommands.exception.LiteException;
import dev.rollczi.litecommands.invocation.Invocation;
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

        Option<BindContextual<SENDER, ?>> bindContextual = MapUtil.findKeyInstanceOf(clazz, this.contextualBindings);

        if (bindContextual.isPresent()) {
            return (Result<T, Object>) bindContextual.get().extract(invocation);
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
