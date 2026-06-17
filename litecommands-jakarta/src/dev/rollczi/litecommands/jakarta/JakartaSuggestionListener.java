package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.annotations.meta.MetaAnnotationKeys;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.suggestion.event.SuggestionResultEvent;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.Validator;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

class JakartaSuggestionListener implements Consumer<SuggestionResultEvent> {

    private static final Logger LOGGER = Logger.getLogger("LiteCommands");

    private final Validator validator;

    JakartaSuggestionListener(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void accept(SuggestionResultEvent event) {
        Argument<?> argument = event.getArgument();
        Parameter parameter = argument.meta().get(MetaAnnotationKeys.SOURCE_PARAMETER);

        Executable executable = parameter.getDeclaringExecutable();
        if (!(executable instanceof Method method)) {
            return;
        }

        int parameterIndex = getParameterIndex(parameter);
        if (parameterIndex == -1) {
            return;
        }

        Object handle = event.getExecutor().getInstance();
        event.getResult().removeIf(suggestion -> {
            Object parsedValue = parse(suggestion.multilevel(), argument.getType().getRawType());
            if (parsedValue == null) {
                return false;
            }

            Object[] parameters = new Object[method.getParameterCount()];
            Class<?>[] parameterTypes = method.getParameterTypes();

            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = i == parameterIndex ? parsedValue : defaultValue(parameterTypes[i]);
            }

            try {
                Set<ConstraintViolation<Object>> violations = validator.forExecutables().validateParameters(handle, method, parameters);

                for (ConstraintViolation<Object> violation : violations) {
                    for (Path.Node node : violation.getPropertyPath()) {
                        if (node.getKind() != ElementKind.PARAMETER) {
                            continue;
                        }

                        Path.ParameterNode parameterNode = node.as(Path.ParameterNode.class);

                        if (parameterNode.getParameterIndex() == parameterIndex) {
                            return true;
                        }
                    }
                }

                return false;
            } catch (IllegalArgumentException e) {
                LOGGER.warning(e.getMessage());
                return false;
            }
        });
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

    private Object defaultValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) return false;
            if (type == char.class) return '\0';
            if (type == byte.class) return (byte) 0;
            if (type == short.class) return (short) 0;
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0.0f;
            if (type == double.class) return 0.0d;
        }

        return null;
    }

    private Object parse(String value, Class<?> type) {
        try {
            if (type == int.class || type == Integer.class) return Integer.parseInt(value);
            if (type == long.class || type == Long.class) return Long.parseLong(value);
            if (type == double.class || type == Double.class) return Double.parseDouble(value);
            if (type == float.class || type == Float.class) return Float.parseFloat(value);
            if (type == short.class || type == Short.class) return Short.parseShort(value);
            if (type == byte.class || type == Byte.class) return Byte.parseByte(value);
            if (type == String.class) return value;
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

}
