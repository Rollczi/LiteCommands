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
        Class<?> typeOfParameter = parameter.getType();

        for (Annotation annotation : parameter.getAnnotations()) {
            Option<AnnotationBind<?, SENDER, ?>> annotationBind = settings.getAnnotationBind(annotation.annotationType(), parameter.getType());

            if (annotationBind.isEmpty()) {
                continue;
            }

            return Option.of(handleExtract(annotationBind.get(), invocation, parameter, annotation));
        }

        return this.settings.getContextualBind(typeOfParameter)
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

        return this.settings.getTypeBind(type)
                .map(typeBind -> typeBind.extract(parameter));
    }

    LiteInjectorSettings<SENDER> settings() {
        return settings;
    }

}
