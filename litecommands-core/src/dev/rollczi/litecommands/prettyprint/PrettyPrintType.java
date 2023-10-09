package dev.rollczi.litecommands.prettyprint;

import dev.rollczi.litecommands.util.StringUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.StringJoiner;

final class PrettyPrintType {

    private static final String TYPE_PATTERN = "{name}{generic}";
    private static final String GENERIC_PATTERN = "<{types}>";

    static String formatType(Type type) {
        String name = type.getTypeName();
        String generic = StringUtil.EMPTY;

        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;

            name = clazz.getSimpleName();

            TypeVariable<? extends Class<?>>[] variables = clazz.getTypeParameters();

            if (variables.length > 0) {
                generic = formatGeneric(variables);
            }
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] arguments = parameterizedType.getActualTypeArguments();

            name = ((Class<?>) parameterizedType.getRawType()).getSimpleName();

            if (arguments.length > 0) {
                generic = formatGeneric(arguments);
            }
        }

        return TYPE_PATTERN
            .replace("{name}", name)
            .replace("{generic}", generic);
    }

    static String formatGeneric(Type[] types) {
        if (types.length == 0) {
            return StringUtil.EMPTY;
        }

        StringJoiner joiner = new StringJoiner(PrettyPrint.NEW_INLINE);

        for (Type type : types) {
            joiner.add(formatType(type));
        }

        return GENERIC_PATTERN.replace("{types}", joiner.toString());
    }

}
