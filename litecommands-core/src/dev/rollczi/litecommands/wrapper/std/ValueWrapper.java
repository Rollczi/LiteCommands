package dev.rollczi.litecommands.wrapper.std;

import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ValueWrapper extends AbstractWrapper<Object> implements Wrapper {

    public ValueWrapper() {
        super(Object.class);
    }

    @Override
    protected <EXPECTED> Supplier<Object> wrapValue(@Nullable EXPECTED valueToWrap, WrapFormat<EXPECTED, ?> info) {
        return new WrapperImpl<>(valueToWrap);
    }

    private static class WrapperImpl<EXPECTED> implements Supplier<Object> {
        private final @Nullable EXPECTED valueToWrap;

        public WrapperImpl(@Nullable EXPECTED valueToWrap) {
            this.valueToWrap = valueToWrap;
        }

        @Override
        public Object get() {
            return valueToWrap;
        }
    }
}
