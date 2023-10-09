package dev.rollczi.litecommands.invocation;

import dev.rollczi.litecommands.util.MapUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InvocationContext {

    private final Map<Class<?>, Object> context = new HashMap<>();

    private InvocationContext() {
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Class<T> type) {
        return (Optional<T>) MapUtil.findByInstanceOf(type, context);
    }

    public static InvocationContext.Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final InvocationContext context = new InvocationContext();

        public <T> Builder put(Class<T> type, T value) {
            context.context.put(type, value);
            return this;
        }

        public Builder putUnsafe(Class<?> type, Object value) {
            context.context.put(type, value);
            return this;
        }

        public InvocationContext build() {
            return context;
        }

    }

}
