package dev.rollczi.litecommands.modern.command.argument.invocation.warpper;

import java.util.function.Supplier;

public interface ExpectedValueWrapperFactory {

    <EXPECTED> ExpectedValueWrapper<EXPECTED> wrap(Class<EXPECTED> type, Supplier<EXPECTED> value);

    Class<?> getWrapperType();

}
