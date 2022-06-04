package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.ArgumentAnnotation;
import org.panda_lang.utilities.inject.DependencyInjection;
import org.panda_lang.utilities.inject.Injector;
import org.panda_lang.utilities.inject.Property;
import panda.std.Option;
import panda.std.function.TriFunction;
import panda.std.stream.PandaStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

class InjectorProvider {

    public static Injector createInjector() {
        return DependencyInjection.createInjector().fork(resources -> {
            resources.on(Object.class).assignHandler(new InjectorHandler<>());

            InjectorHandler<Annotation> handler = new InjectorHandler<>()
                    .withCorrector(Boolean.class, boolean.class, Boolean::booleanValue)
                    .withCorrector(Byte.class, byte.class, Byte::byteValue)
                    .withCorrector(Short.class, short.class, Short::shortValue)
                    .withCorrector(Integer.class, int.class, Integer::intValue)
                    .withCorrector(Long.class, long.class, Long::longValue)
                    .withCorrector(Float.class, float.class, Float::floatValue)
                    .withCorrector(Double.class, double.class, Double::doubleValue)
                    .withCorrector(Character.class, char.class, Character::charValue);

            resources.on(boolean.class).assignHandler(handler::apply);
            resources.on(byte.class).assignHandler(handler::apply);
            resources.on(short.class).assignHandler(handler::apply);
            resources.on(int.class).assignHandler(handler::apply);
            resources.on(long.class).assignHandler(handler::apply);
            resources.on(float.class).assignHandler(handler::apply);
            resources.on(double.class).assignHandler(handler::apply);
            resources.on(char.class).assignHandler(handler::apply);
        });
    }

    private final static class InjectorHandler<A extends Annotation> implements TriFunction<Property, A, Object[], Object> {

        private final Set<TypeCorrector<?, ?>> correctors = new HashSet<>();

        public <FROM, TO> InjectorHandler<A> withCorrector(Class<FROM> from, Class<TO> to, Function<FROM, TO> corrector) {
            correctors.add(new TypeCorrector<>(from, to, corrector));
            return this;
        }

        @Override
        public Object apply(Property property, A annotation, Object[] args) {
            Option<Parameter> parameterOption = property.getParameter();

            if (parameterOption.isEmpty()) {
                return null;
            }

            Parameter parameter = parameterOption.get();
            Executable executable = parameter.getDeclaringExecutable();

            if (executable instanceof Method) {
                Method method = (Method) executable;

                int index = 0;
                for (Parameter methodParameter : method.getParameters()) {
                    Option<Annotation> option = PandaStream.of(methodParameter.getAnnotations())
                            .find(a -> a.annotationType().isAnnotationPresent(ArgumentAnnotation.class));

                    if (option.isEmpty()) {
                        continue;
                    }

                    if (methodParameter.equals(parameter)) {
                        Object toInject = InvokeContext.fromArgs(args).getParameter(index);

                        if (parameter.getType().isAssignableFrom(toInject.getClass())) {
                            return toInject;
                        }

                        for (TypeCorrector<?, ?> corrector : correctors) {
                            if (corrector.getFrom().isAssignableFrom(toInject.getClass())) {
                                return corrector.correct(toInject);
                            }
                        }

                        throw new IllegalArgumentException("Cannot inject " + toInject.getClass() + " to " + parameter.getType());
                    }

                    index++;
                }
            }

            return null;
        }
    }

    private final static class TypeCorrector<T, S> {
        private final Class<T> from;
        private final Class<S> to;
        private final Function<T, S> corrector;

        private TypeCorrector(Class<T> from, Class<S> to, Function<T, S> corrector) {
            this.from = from;
            this.to = to;
            this.corrector = corrector;
        }

        Class<T> getFrom() {
            return from;
        }

        Class<S> getTo() {
            return to;
        }

        boolean canCorrect(Class<?> type) {
            return from.isAssignableFrom(type);
        }

        S correct(Object value) {
            return corrector.apply((T) value);
        }

    }

}
