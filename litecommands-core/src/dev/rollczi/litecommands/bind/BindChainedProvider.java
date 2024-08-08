package dev.rollczi.litecommands.bind;

import java.util.function.Function;

public interface BindChainedProvider<T> extends Function<BindChainAccessor, T> {

}
