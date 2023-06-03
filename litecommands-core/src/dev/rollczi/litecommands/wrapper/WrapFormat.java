package dev.rollczi.litecommands.wrapper;

public class WrapFormat<PARSED, OUT> {

    private final Class<PARSED> parsedType;
    private final Class<OUT> outType;

    private WrapFormat(Class<PARSED> parsedType, Class<OUT> outType) {
        this.parsedType = parsedType;
        this.outType = outType;
    }

    public Class<PARSED> getParsedType() {
        return parsedType;
    }

    public boolean hasOutType() {
        return outType != null;
    }

    public Class<OUT> getOutType() {
        if (outType == null) {
            throw new IllegalStateException("Wrapper type is not defined");
        }

        return outType;
    }

    public static <PARSED, OUT> WrapFormat<PARSED, OUT> of(Class<PARSED> type, Class<OUT> toWrapperType) {
        if (toWrapperType == null) {
            throw new IllegalArgumentException("Wrapper type cannot be null");
        }

        return new WrapFormat<>(type, toWrapperType);
    }

    public static <PARSED> WrapFormat<PARSED, PARSED> notWrapped(Class<PARSED> type) {
        return new WrapFormat<>(type, null);
    }

}
