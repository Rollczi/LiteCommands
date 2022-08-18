package dev.rollczi.litecommands.injector;

import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.injector.bind.AnnotationBind;
import dev.rollczi.litecommands.injector.bind.TypeBind;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

public interface InjectorSettings<CONTEXT> {

    <T> InjectorSettings<CONTEXT> typeBind(Class<T> type, Supplier<T> supplier);

    InjectorSettings<CONTEXT> typeUnsafeBind(Class<?> type, TypeBind<?> supplier);

    <T> InjectorSettings<CONTEXT> typeBind(Class<T> type, TypeBind<T> typeBind);

    <T, A extends Annotation> InjectorSettings<CONTEXT> annotationBind(Class<T> type, Class<A> on, AnnotationBind<T, CONTEXT, A> annotationBind);

    <T> InjectorSettings<CONTEXT> contextualBind(Class<T> on, Contextual<CONTEXT, T> contextual);

    Injector<CONTEXT> create();

}
