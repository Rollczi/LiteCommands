package dev.rollczi.litecommands.implementation.injector;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.handle.LiteException;
import panda.std.Option;

import java.util.Map;
import java.util.function.Supplier;

class InjectorContextProcessor<SENDER> {

    private final LiteInjectorSettings<SENDER> settings;

    InjectorContextProcessor(LiteInjectorSettings<SENDER> settings) {
        this.settings = settings;
    }

    <T> Option<T> extract(Class<T> type, Invocation<SENDER> invocation) {
        Map<Class<?>, Contextual<SENDER, ?>> binds = this.settings.getContextualBinds();
        Contextual<SENDER, ?> contextual = binds.get(type);

        if (contextual != null) {
            return Option.of(type.cast(contextual.extract(invocation.handle(), invocation)
                    .orThrow(LiteException::new)));
        }

        for (Map.Entry<Class<?>, Contextual<SENDER, ?>> entry : binds.entrySet()) {
            Class<?> bindType = entry.getKey();

            if (bindType.isAssignableFrom(type)) {
                return Option.of(type.cast(entry.getValue().extract(invocation.handle(), invocation)
                        .orThrow(LiteException::new)));
            }
        }

        return this.extract(type);
    }

    <T> Option<T> extract(Class<T> type) {
        Map<Class<?>, Supplier<?>> binds = this.settings.getTypeBinds();
        Supplier<?> supplier = binds.get(type);

        if (supplier != null) {
            return Option.of(supplier.get()).is(type);
        }

        for (Map.Entry<Class<?>, Supplier<?>> entry : binds.entrySet()) {
            Class<?> bindType = entry.getKey();

            if (bindType.isAssignableFrom(type)) {
                return Option.of(entry.getValue().get()).is(type);
            }
        }

        return Option.none();
    }

    LiteInjectorSettings<SENDER> settings() {
        return settings;
    }

}
