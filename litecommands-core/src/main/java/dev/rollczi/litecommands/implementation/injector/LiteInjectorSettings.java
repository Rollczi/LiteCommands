package dev.rollczi.litecommands.implementation.injector;

import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.injector.InjectorSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

class LiteInjectorSettings<SENDER> implements InjectorSettings<SENDER> {

    private final Map<Class<?>, Supplier<?>> typeBinds = new HashMap<>();
    private final Map<Class<?>, Contextual<SENDER, ?>> contextualBinds = new HashMap<>();

    @Override
    public <T> LiteInjectorSettings<SENDER> typeBind(Class<T> type, Supplier<T> supplier) {
        this.typeBinds.put(type, supplier);
        return this;
    }

    @Override
    public <T> InjectorSettings<SENDER> contextualBind(Class<T> on, Contextual<SENDER, T> contextual) {
        this.contextualBinds.put(on, contextual);
        return this;
    }

    @Override
    public Injector<SENDER> create() {
        return new CommandInjector<>(this);
    }

    LiteInjectorSettings<SENDER> duplicate() {
        LiteInjectorSettings<SENDER> settings = new LiteInjectorSettings<>();

        settings.typeBinds.putAll(this.typeBinds);
        settings.contextualBinds.putAll(this.contextualBinds);

        return settings;
    }

    Map<Class<?>, Contextual<SENDER, ?>> getContextualBinds() {
        return contextualBinds;
    }

    Map<Class<?>, Supplier<?>> getTypeBinds() {
        return typeBinds;
    }

}
