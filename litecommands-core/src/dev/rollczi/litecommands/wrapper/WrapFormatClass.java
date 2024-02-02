package dev.rollczi.litecommands.wrapper;

import dev.rollczi.litecommands.reflect.type.TypeToken;

class WrapFormatClass<PARSED, OUT> implements WrapFormat<PARSED, OUT> {

    private final Class<PARSED> parsedType;
    private final Class<OUT> outType;

    WrapFormatClass(Class<PARSED> parsedType, Class<OUT> outType) {
        this.parsedType = parsedType;
        this.outType = outType;
    }

    @Override
    public Class<PARSED> getParsedType() {
        return parsedType;
    }

    @Override
    public TypeToken<PARSED> parsedType() {
        return TypeToken.of(parsedType);
    }

    @Override
    public boolean hasOutType() {
        return outType != null;
    }

    @Override
    public Class<?> getOutTypeOrParsed() {
        return outType != null ? outType : parsedType;
    }

    @Override
    public TypeToken<?> outOrElseParsed() {
        return outType != null ? TypeToken.of(outType) : TypeToken.of(parsedType);
    }

    @Override
    public Class<OUT> getOutType() {
        if (outType == null) {
            throw new IllegalStateException("Wrapper type is not defined");
        }

        return outType;
    }

    @Override
    public TypeToken<OUT> outType() {
        return TypeToken.of(getOutType());
    }

}
