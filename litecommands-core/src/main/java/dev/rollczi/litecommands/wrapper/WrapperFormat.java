package dev.rollczi.litecommands.wrapper;

public class WrapperFormat<T> {

    private final Class<T> type;
    private final Class<?> toWrapperType;

    public WrapperFormat(Class<T> type, Class<?> toWrapperType) {
        this.type = type;
        this.toWrapperType = toWrapperType;
    }

    public Class<T> getType() {
        return type;
    }

    public Class<?> getWrapperType() {
        return toWrapperType;
    }

}
