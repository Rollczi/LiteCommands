package dev.rollczi.litecommands.wrapper;

import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.shared.Preconditions;

public interface WrapFormat<PARSED, OUT> {

    Class<PARSED> getParsedType();

    TypeToken<PARSED> parsedType();

    boolean hasOutType();

    Class<?> getOutTypeOrParsed();

    TypeToken<?> outOrElseParsed();

    Class<OUT> getOutType();

    TypeToken<OUT> outType();

    static <PARSED, OUT> WrapFormat<PARSED, OUT> of(Class<PARSED> type, Class<OUT> toWrapperType) {
        if (toWrapperType == null) {
            throw new IllegalArgumentException("Wrapper type cannot be null");
        }

        return new WrapFormatClass<>(type, toWrapperType);
    }

    static <PARSED> WrapFormat<PARSED, PARSED> notWrapped(Class<PARSED> type) {
        return new WrapFormatClass<>(type, null);
    }

    static <PARSED, OUT> WrapFormat<PARSED, OUT> of(TypeToken<PARSED> type, TypeToken<OUT> toWrapperType) {
        Preconditions.notNull(type, "type");
        Preconditions.notNull(toWrapperType, "toWrapperType");

        return new WrapFormatTypeToken<>(type, toWrapperType);
    }

    static <PARSED> WrapFormat<PARSED, PARSED> notWrapped(TypeToken<PARSED> type) {
        return new WrapFormatTypeToken<>(type, null);
    }

}
