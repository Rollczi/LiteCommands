package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.util.MapUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ContextRegistry<SENDER> implements ContextChainAccessor<SENDER>  {

    private final Map<Class<?>, ContextChainedProvider<SENDER, ?>> contextualBindings = new HashMap<>();

    public <T> void registerProvider(Class<T> clazz, ContextChainedProvider<SENDER, T> supplier) {
        this.contextualBindings.put(clazz, supplier);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ContextResult<T> provideContext(Class<T> clazz, Invocation<SENDER> invocation) {
        Optional<ContextChainedProvider<SENDER, ?>> bindContextual = MapUtil.findByInstanceOf(clazz, this.contextualBindings);

        if (bindContextual.isPresent()) {
            return (ContextResult<T>) bindContextual.get().provide(invocation, this);
        }

        return ContextResult.error("Cannot find binding for " + clazz.getName());
    }

}
