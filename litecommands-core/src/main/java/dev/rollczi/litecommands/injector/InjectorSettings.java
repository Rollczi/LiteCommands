package dev.rollczi.litecommands.injector;

import dev.rollczi.litecommands.contextual.Contextual;

import java.util.function.Supplier;

public interface InjectorSettings<CONTEXT> {

    <T> InjectorSettings<CONTEXT> typeBind(Class<T> type, Supplier<T> supplier);

    <T> InjectorSettings<CONTEXT> contextualBind(Class<T> on, Contextual<CONTEXT, T> contextual);

    Injector<CONTEXT> create();

}
