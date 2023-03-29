package dev.rollczi.litecommands.wrapper;

public interface WrappedExpectedFactory {

    <EXPECTED> WrappedExpected<EXPECTED> create(
        ValueToWrap<EXPECTED> valueToWrap,
        WrapperFormat<EXPECTED, ?> info
    );

    default <EXPECTED> WrappedExpected<EXPECTED> createEmpty(WrapperFormat<EXPECTED, ?> info) {
        throw new UnsupportedOperationException("Cannot create empty value");
    }

    default boolean canCreateEmpty() {
        return false;
    }

    Class<?> getWrapperType();

}
