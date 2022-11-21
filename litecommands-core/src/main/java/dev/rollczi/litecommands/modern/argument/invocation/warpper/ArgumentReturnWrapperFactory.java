package dev.rollczi.litecommands.modern.argument.invocation.warpper;

public interface ArgumentReturnWrapperFactory<T> {

    ArgumentReturnWrapper<T> wrap(T value);

}
