package dev.rollczi.litecommands.wrapper;

public class WrapperFormat<T, WRAPPER> {

    private final Class<T> type;
    private final Class<?> toWrapperType;

    private WrapperFormat(Class<T> type, Class<?> toWrapperType) {
        this.type = type;
        this.toWrapperType = toWrapperType;
    }

    public Class<T> getType() {
        return type;
    }

    public boolean hasWrapper() {
        return toWrapperType != Void.class;
    }

    public Class<?> getWrapperType() {
        if (toWrapperType == Void.class) {
            throw new IllegalStateException("Wrapper type is not defined");
        }

        return toWrapperType;
    }

    public static <T, WRAPPER> WrapperFormat<T, WRAPPER> of(Class<T> type, Class<WRAPPER> toWrapperType) {
        if (toWrapperType == null) {
            throw new IllegalArgumentException("Wrapper type cannot be null");
        }

        return new WrapperFormat<>(type, toWrapperType);
    }

    public static <T> WrapperFormat<T, Void> notWrapped(Class<T> type) {
        return new WrapperFormat<>(type, Void.class);
    }
}
