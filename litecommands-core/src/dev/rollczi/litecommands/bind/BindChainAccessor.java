package dev.rollczi.litecommands.bind;

public interface BindChainAccessor {

    <T> BindResult<T> getInstance(Class<T> clazz);

}
