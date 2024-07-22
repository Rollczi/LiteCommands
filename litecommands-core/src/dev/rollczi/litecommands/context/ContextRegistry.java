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
            ContextResult<T> result = (ContextResult<T>) bindContextual.get().provide(invocation, this);

            Object failedReason = result.getFailedReason();
            if (failedReason instanceof ContextResult<?>) {
                return ContextResult.error(((ContextResult<?>) failedReason).getFailedReason());
            }

            return result;
        }

        return ContextResult.error("Cannot find binding for " + clazz.getName());
    }

}
