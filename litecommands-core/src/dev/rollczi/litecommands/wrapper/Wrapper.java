package dev.rollczi.litecommands.wrapper;

public interface Wrapper {

    <EXPECTED> Wrap<EXPECTED> create(
        ValueToWrap<EXPECTED> valueToWrap,
        WrapFormat<EXPECTED, ?> info
    );

    default <EXPECTED> Wrap<EXPECTED> createEmpty(WrapFormat<EXPECTED, ?> info) {
        throw new UnsupportedOperationException("Cannot create empty value");
    }

    default boolean canCreateEmpty() {
        return false;
    }

    Class<?> getWrapperType();

}
