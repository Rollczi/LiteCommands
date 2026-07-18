package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.annotations.meta.MetaAnnotationKeys;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.event.SuggestionResultEvent;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

class JakartaSuggestionListener implements Consumer<SuggestionResultEvent> {

    private static final Logger LOGGER = Logger.getLogger("LiteCommands");

    private final Validator validator;

    JakartaSuggestionListener(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void accept(SuggestionResultEvent event) {
        Argument<?> argument = event.argument();
        Parameter parameter = argument.meta().get(MetaAnnotationKeys.SOURCE_PARAMETER);

        Executable executable = parameter.getDeclaringExecutable();
        if (!(executable instanceof Method method)) {
            return;
        }

        int parameterIndex = getParameterIndex(parameter);
        if (parameterIndex == -1) {
            return;
        }

        Object handle = event.executor().getInstance();

        event.result().removeIf(suggestion ->
            !isValidSuggestion(suggestion, argument, method, handle, parameterIndex)
        );
    }

    private boolean isValidSuggestion(
        Suggestion suggestion,
        Argument<?> argument,
        Method method,
        Object handle,
        int parameterIndex
    ) {
        Object parsedValue = Parser.parse(
            suggestion.multilevel(),
            argument.getType().getRawType()
        );

        if (parsedValue == null) {
            return false;
        }

        Object[] parameters = buildParameters(method, parameterIndex, parsedValue);

        try {
            Set<ConstraintViolation<Object>> violations =
                validator.forExecutables().validateParameters(handle, method, parameters);

            return !hasViolationForParameter(violations, parameterIndex);

        } catch (IllegalArgumentException e) {
            LOGGER.warning(e.getMessage());
            return false;
        }
    }

    private Object[] buildParameters(Method method, int index, Object value) {
        Class<?>[] types = method.getParameterTypes();
        Object[] params = new Object[types.length];

        for (int i = 0; i < types.length; i++) {
            params[i] = (i == index) ? value : Defaults.get(types[i]);
        }

        return params;
    }

    private boolean hasViolationForParameter(
        Set<ConstraintViolation<Object>> violations,
        int parameterIndex
    ) {
        for (ConstraintViolation<Object> violation : violations) {
            for (Path.Node node : violation.getPropertyPath()) {

                if (node.getKind() != ElementKind.PARAMETER) {
                    continue;
                }

                if (((Path.ParameterNode) node).getParameterIndex() == parameterIndex) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getParameterIndex(Parameter parameter) {
        Parameter[] parameters = parameter.getDeclaringExecutable().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].equals(parameter)) {
                return i;
            }
        }
        return -1;
    }

    private static final class Parser {

        private static final Map<Class<?>, Function<String, ?>> PARSERS = Map.of(
            Byte.class, Byte::parseByte,
            Short.class, Short::parseShort,
            Integer.class, Integer::parseInt,
            Long.class, Long::parseLong,
            Float.class, Float::parseFloat,
            Double.class, Double::parseDouble,
            Boolean.class, Boolean::parseBoolean,
            Character.class, s -> s.isEmpty() ? null : s.charAt(0),
            String.class, Function.identity()
        );

        @SuppressWarnings("unchecked")
        @Nullable
        private static <T> T parse(String value, Class<T> type) {
            Function<String, ?> parser = PARSERS.get(normalize(type));

            if (parser == null) {
                return null;
            }

            try {
                return (T) parser.apply(value);
            } catch (RuntimeException e) {
                return null;
            }
        }

        private static Class<?> normalize(Class<?> type) {
            if (!type.isPrimitive()) {
                return type;
            }

            // When java 21 comes, switch to type matching `(switch(type))`
            return switch (type.getName()) {
                case "byte" -> Byte.class;
                case "short" -> Short.class;
                case "int" -> Integer.class;
                case "long" -> Long.class;
                case "float" -> Float.class;
                case "double" -> Double.class;
                case "boolean" -> Boolean.class;
                case "char" -> Character.class;
                default -> type;
            };
        }
    }

    private static final class Defaults {

        private static final Map<Class<?>, Object> DEFAULTS = Map.of(
            boolean.class, false,
            char.class, '\0',
            byte.class, (byte) 0,
            short.class, (short) 0,
            int.class, 0,
            long.class, 0L,
            float.class, 0f,
            double.class, 0d
        );

        @Nullable
        private static Object get(Class<?> type) {
            return DEFAULTS.get(type);
        }
    }
}
