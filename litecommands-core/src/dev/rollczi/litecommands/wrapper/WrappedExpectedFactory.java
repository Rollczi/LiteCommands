package dev.rollczi.litecommands.wrapper;

public interface WrappedExpectedFactory {

    <EXPECTED> Wrapped<EXPECTED> create(
        ValueToWrap<EXPECTED> valueToWrap,
        WrapperFormat<EXPECTED, ?> info
    );

    default <EXPECTED> Wrapped<EXPECTED> createEmpty(WrapperFormat<EXPECTED, ?> info) {
        throw new UnsupportedOperationException("Cannot create empty value");
    }

    default boolean canCreateEmpty() {
        return false;
    }

    Class<?> getWrapperType();

}
