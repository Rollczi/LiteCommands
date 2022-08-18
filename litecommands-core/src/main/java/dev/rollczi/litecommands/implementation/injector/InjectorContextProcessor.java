package dev.rollczi.litecommands.implementation.injector;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.handle.LiteException;
import dev.rollczi.litecommands.injector.bind.AnnotationBind;
import dev.rollczi.litecommands.injector.bind.TypeBind;
import dev.rollczi.litecommands.shared.MapUtil;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Map;

class InjectorContextProcessor<SENDER> {

    private final LiteInjectorSettings<SENDER> settings;

    InjectorContextProcessor(LiteInjectorSettings<SENDER> settings) {
        this.settings = settings;
    }

    Option<?> extract(Parameter parameter, Invocation<SENDER> invocation) {
        Class<?> type = parameter.getType();
        Map<Class<? extends Annotation>, Map<Class<?>, AnnotationBind<?, SENDER, ?>>> annotationBinds = settings.getAnnotationBinds();

        for (Annotation annotation : parameter.getAnnotations()) {
            Map<Class<?>, AnnotationBind<?, SENDER, ?>> bindsByType = annotationBinds.get(annotation.annotationType());

            if (bindsByType == null) {
                continue;
            }

            AnnotationBind<?, SENDER, ?> annotationBind = bindsByType.get(parameter.getType());

            if (annotationBind == null) {
                continue;
            }

            return Option.of(handleExtract(annotationBind, invocation, parameter, annotation));
        }

        Map<Class<?>, Contextual<SENDER, ?>> binds = this.settings.getContextualBinds();

        return MapUtil.findInstanceOf(type, binds)
                .map(contextual -> contextual.extract(invocation.handle(), invocation))
                .<Object>map(result -> result.orThrow(LiteException::new))
                .orElse(() -> this.extract(parameter));
    }

    @SuppressWarnings("unchecked")
    private <TYPE, ANNOTATION extends Annotation> Object handleExtract(AnnotationBind<TYPE, SENDER, ANNOTATION> annotationBind, Invocation<SENDER> invocation, Parameter parameter, Annotation annotation) {
        return annotationBind.extract(invocation, parameter, (ANNOTATION) annotation);
    }

    Option<Object> extract(Parameter parameter) {
        Class<?> type = parameter.getType();
        Map<Class<?>, TypeBind<?>> binds = this.settings.getTypeBinds();

        return MapUtil.findInstanceOf(type, binds)
                .map(typeBind -> typeBind.extract(parameter));
    }

    LiteInjectorSettings<SENDER> settings() {
        return settings;
    }

}
