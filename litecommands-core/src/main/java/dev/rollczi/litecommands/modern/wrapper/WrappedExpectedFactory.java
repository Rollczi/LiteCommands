package dev.rollczi.litecommands.modern.wrapper;

import panda.std.Option;

public interface WrappedExpectedFactory {

    <EXPECTED> WrappedExpected<EXPECTED> wrap(
        ValueToWrap<EXPECTED> valueToWrap,
        WrapperFormat<EXPECTED> info
    );

    <EXPECTED> Option<WrappedExpected<EXPECTED>> empty(WrapperFormat<EXPECTED> info);

    Class<?> getWrapperType();

}
