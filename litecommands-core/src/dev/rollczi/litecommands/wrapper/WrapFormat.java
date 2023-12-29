package dev.rollczi.litecommands.wrapper;

import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.shared.Preconditions;

public interface WrapFormat<PARSED, OUT> {

    @Deprecated
    Class<PARSED> getParsedType();

    TypeToken<PARSED> parsedType();

    boolean hasOutType();

    @Deprecated
    Class<?> getOutTypeOrParsed();

    TypeToken<?> outOrElseParsed();

    @Deprecated
    Class<OUT> getOutType();

    TypeToken<OUT> outType();

    @Deprecated
    static <PARSED, OUT> WrapFormat<PARSED, OUT> of(Class<PARSED> type, Class<OUT> toWrapperType) {
        if (toWrapperType == null) {
            throw new IllegalArgumentException("Wrapper type cannot be null");
        }

        return new WrapFormatClass<>(type, toWrapperType);
    }

    @Deprecated
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
