package dev.rollczi.litecommands.wrapper;

public class WrapperFormat<PARSED, OUT> {

    private final Class<PARSED> parsedType;
    private final Class<OUT> outType;

    private WrapperFormat(Class<PARSED> parsedType, Class<OUT> outType) {
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

    public static <PARSED, OUT> WrapperFormat<PARSED, OUT> of(Class<PARSED> type, Class<OUT> toWrapperType) {
        if (toWrapperType == null) {
            throw new IllegalArgumentException("Wrapper type cannot be null");
        }

        return new WrapperFormat<>(type, toWrapperType);
    }

    public static <PARSED> WrapperFormat<PARSED, PARSED> notWrapped(Class<PARSED> type) {
        return new WrapperFormat<>(type, null);
    }

}
