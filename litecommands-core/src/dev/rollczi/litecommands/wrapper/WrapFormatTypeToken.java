package dev.rollczi.litecommands.wrapper;

import dev.rollczi.litecommands.reflect.type.TypeToken;

public class WrapFormatTypeToken<PARSED, OUT> implements WrapFormat<PARSED, OUT> {

    private final TypeToken<PARSED> parsedType;
    private final TypeToken<OUT> outType;

    public WrapFormatTypeToken(TypeToken<PARSED> type, TypeToken<OUT> toWrapperType) {
        this.parsedType = type;
        this.outType = toWrapperType;
    }

    @Override
    public Class<PARSED> getParsedType() {
        return parsedType.getRawType();
    }

    @Override
    public TypeToken<PARSED> parsedType() {
        return parsedType;
    }

    @Override
    public boolean hasOutType() {
        return outType != null;
    }

    @Override
    public Class<?> getOutTypeOrParsed() {
        return outType != null ? outType.getRawType() : parsedType.getRawType();
    }

    @Override
    public TypeToken<OUT> outType() {
        return outType;
    }

    @Override
    public TypeToken<?> outOrElseParsed() {
        return outType != null ? outType : parsedType;
    }

    @Override
    public Class<OUT> getOutType() {
        if (outType == null) {
            throw new IllegalStateException("Wrapper type is not defined");
        }

        return outType.getRawType();
    }

}
