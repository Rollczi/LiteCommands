package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.util.MapUtil;
import panda.std.Option;
import panda.std.Result;

import java.util.HashMap;
import java.util.Map;

public class ContextRegistry<SENDER> {

    private final Map<Class<?>, ContextProvider<SENDER, ?>> contextualBindings = new HashMap<>();

    public <T> void registerProvider(Class<T> clazz, ContextProvider<SENDER, T> supplier) {
        this.contextualBindings.put(clazz, supplier);
    }

    @SuppressWarnings("unchecked")
    public <T> ContextResult<T> provideContext(Class<T> clazz, Invocation<SENDER> invocation) {
        Option<ContextProvider<SENDER, ?>> bindContextual = MapUtil.findByInstanceOf(clazz, this.contextualBindings);

        if (bindContextual.isPresent()) {
            return (ContextResult<T>) bindContextual.get().provide(invocation);
        }

        return ContextResult.error("Cannot find binding for " + clazz.getName());
    }

}
