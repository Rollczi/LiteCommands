package dev.rollczi.litecommands.wrapper;

public class WrapperFormat<PARSED, OUT> {

    private final Class<PARSED> type;
    private final Class<OUT> toWrapperType;

    private WrapperFormat(Class<PARSED> type, Class<OUT> toWrapperType) {
        this.type = type;
        this.toWrapperType = toWrapperType;
    }

    public Class<PARSED> getType() {
        return type;
    }

    public boolean hasWrapper() {
        return toWrapperType != null;
    }

    public Class<OUT> getWrapperType() {
        if (toWrapperType == null) {
            throw new IllegalStateException("Wrapper type is not defined");
        }

        return toWrapperType;
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
