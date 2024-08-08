package dev.rollczi.litecommands.bind;

import java.util.function.Supplier;

public interface BindProvider<T> extends BindChainedProvider<T>, Supplier<T> {

    @Override
    default T apply(BindChainAccessor bindChainAccessor) {
        return this.get();
    }

}
