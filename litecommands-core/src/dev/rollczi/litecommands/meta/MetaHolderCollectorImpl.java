package dev.rollczi.litecommands.meta;

import java.util.ArrayList;
import java.util.List;

public class MetaHolderCollectorImpl implements MetaCollector {

    private final MetaHolder metaHolder;

    public MetaHolderCollectorImpl(MetaHolder metaHolder) {
        this.metaHolder = metaHolder;
    }

    @Override
    public <T> List<T> collect(MetaKey<T> key) {
        List<T> collected = new ArrayList<>();
        MetaHolder current = metaHolder;

        while (current != null) {
            if (current.meta().has(key)) {
                collected.add(current.meta().get(key));
            }

            current = current.parentMeta();
        }

        return collected;
    }

}
