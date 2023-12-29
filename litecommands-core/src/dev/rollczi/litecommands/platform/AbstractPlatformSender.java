package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.meta.MetaKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractPlatformSender implements PlatformSender {

    protected final Map<MetaKey<?>, Supplier<?>> metaMap = new HashMap<>();

    public AbstractPlatformSender() {
        this.putProperty(PlatformSender.NAME, () -> this.getName());
        this.putProperty(PlatformSender.DISPLAY_NAME, () -> this.getDisplayName());
        this.putProperty(PlatformSender.IDENTIFIER, () -> this.getIdentifier());
    }

    @Override
    public String getDisplayName() {
        return this.getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProperty(MetaKey<T> key) {
        Supplier<?> supplier = metaMap.get(key);

        if (supplier == null) {
            return null;
        }

        return (T) supplier.get();
    }

    protected <T> void putProperty(MetaKey<T> key, Supplier<T> supplier) {
        metaMap.put(key, supplier);
    }

}
