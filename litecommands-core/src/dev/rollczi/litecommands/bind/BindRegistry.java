package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.util.MapUtil;
import java.util.function.Function;
import panda.std.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class BindRegistry implements BindChainAccessor {

    private final Map<Class<?>, Function<BindChainAccessor, ?>> instanceBindings = new HashMap<>();

    public <T> void bindInstance(Class<T> on, Function<BindChainAccessor, T> bind) {
        this.instanceBindings.put(on, bind);
    }

    public <T> void bindInstance(Class<T> on, Supplier<T> bind) {
        this.instanceBindings.put(on, chainAccessor -> bind.get());
    }

    public void bindInstanceUnsafe(Class<?> on, Supplier<?> bind) {
        this.instanceBindings.put(on, chainAccessor -> bind.get());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Result<T, String> getInstance(Class<T> clazz) {
        Optional<Function<BindChainAccessor, ?>> option = MapUtil.findByInstanceOf(clazz, this.instanceBindings);

        if (option.isPresent()) {
            Function<BindChainAccessor, T> supplier = (Function<BindChainAccessor, T>) option.get();

            return Result.ok(supplier.apply(this));
        }

        return Result.error("Cannot find binding for " + clazz.getName());
    }

}
