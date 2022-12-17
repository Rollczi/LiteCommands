package dev.rollczi.litecommands.implementation.injector;

import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.injector.InjectorSettings;
import dev.rollczi.litecommands.injector.bind.AnnotationBind;
import dev.rollczi.litecommands.injector.bind.TypeBind;
import dev.rollczi.litecommands.shared.MapUtil;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

class LiteInjectorSettings<SENDER> implements InjectorSettings<SENDER> {

    private final Map<Class<?>, TypeBind<?>> typeBinds = new HashMap<>();
    private final Map<Class<?>, Contextual<SENDER, ?>> contextualBinds = new HashMap<>();
    private final Map<Class<? extends Annotation>, Map<Class<?>, AnnotationBind<?, SENDER, ?>>> annotationBinds = new HashMap<>();

    @Override
    public <T> LiteInjectorSettings<SENDER> typeBind(Class<T> type, Supplier<T> supplier) {
        this.typeBinds.put(type, parameter -> supplier.get());
        return this;
    }

    @Override
    public InjectorSettings<SENDER> typeUnsafeBind(Class<?> type, TypeBind<?> supplier) {
        this.typeBinds.put(type, supplier);
        return this;
    }

    @Override
    public <T> InjectorSettings<SENDER> typeBind(Class<T> type, TypeBind<T> typeBind) {
        this.typeBinds.put(type, typeBind);
        return this;
    }

    @Override
    public <T, A extends Annotation> InjectorSettings<SENDER> annotationBind(Class<T> type, Class<A> on, AnnotationBind<T, SENDER, A> annotationBind) {
        this.annotationBinds.computeIfAbsent(on, k -> new HashMap<>()).put(type, annotationBind);
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
        settings.annotationBinds.putAll(this.annotationBinds);

        return settings;
    }

    Option<TypeBind<?>> getTypeBind(Class<?> type) {
        return MapUtil.findInstanceOf(type, typeBinds);
    }

    Option<Contextual<SENDER, ?>> getContextualBind(Class<?> type) {
        return MapUtil.findInstanceOf(type, contextualBinds);
    }
    
    Option<AnnotationBind<?, SENDER, ?>> getAnnotationBind(Class<? extends Annotation> annotation, Class<?> type) {
        Map<Class<?>, AnnotationBind<?, SENDER, ?>> binds = annotationBinds.get(annotation);
        if (binds == null) {
            return Option.none();
        }

        return Option.of(binds.get(type));
    }

}
