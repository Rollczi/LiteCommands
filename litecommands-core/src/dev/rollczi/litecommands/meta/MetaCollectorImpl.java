package dev.rollczi.litecommands.meta;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MetaCollectorImpl implements MetaCollector {

    private final Collection<Meta> metas;

    public MetaCollectorImpl(Collection<Meta> metas) {
        this.metas = metas;
    }

    @Override
    public <T> List<T> collect(MetaKey<T> key) {
        return metas.stream()
            .filter(meta -> meta.has(key))
            .map(meta -> meta.get(key))
            .collect(Collectors.toList());
    }

}
