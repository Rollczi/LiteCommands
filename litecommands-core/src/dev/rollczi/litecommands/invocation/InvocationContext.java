package dev.rollczi.litecommands.invocation;

import dev.rollczi.litecommands.util.MapUtil;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;

public class InvocationContext {

    private final Map<Class<?>, Object> context = new HashMap<>();

    private InvocationContext() {
    }

    @SuppressWarnings("unchecked")
    public <T> Option<T> get(Class<T> type) {
        return (Option<T>) MapUtil.findKeyInstanceOf(type, context);
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

        public InvocationContext build() {
            return context;
        }

    }

}
