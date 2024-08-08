package dev.rollczi.litecommands.bind;

import panda.std.Result;

public interface BindChainAccessor {

    <T> Result<T, String> getInstance(Class<T> clazz);

}
