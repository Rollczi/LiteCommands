package dev.rollczi.litecommands.reflect.type;

import dev.rollczi.litecommands.reflect.IterableSuperClassResolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TypeIndex<T> {

    private final Map<Class<?>, T> sameIndex = new HashMap<>();
    private final Map<Class<?>, T> downwardIndex = new HashMap<>();
    private final Map<Class<?>, T> upwardIndex = new HashMap<>();

    public void put(TypeRange<?> range, T value) {
        if (range instanceof TypeRange.SameTypeRange) {
            TypeRange.SameTypeRange<?> sameTypeRange = (TypeRange.SameTypeRange<?>) range;
            sameIndex.put(sameTypeRange.getSame(), value);
            return;
        }

        if (range instanceof TypeRange.DownwardsTypeRange) {
            TypeRange.DownwardsTypeRange<?> downwardsTypeRange = (TypeRange.DownwardsTypeRange<?>) range;

            for (Class<?> aClass : downwardsTypeRange.getInclude()) {
                downwardIndex.put(aClass, value);
            }

            return;
        }

        if (range instanceof TypeRange.UpwardsTypeRange) {
            TypeRange.UpwardsTypeRange<?> upwardsTypeRange = (TypeRange.UpwardsTypeRange<?>) range;
            upwardIndex.put(upwardsTypeRange.getType(), value);
        }
    }

    public Iterable<T> get(Class<?> type) {
        return new TypeIterable(type);
    }

    public List<T> computeIfAbsent(TypeRange<?> range, Supplier<T> supplier) {
        if (range instanceof TypeRange.SameTypeRange) {
            TypeRange.SameTypeRange<?> sameTypeRange = (TypeRange.SameTypeRange<?>) range;
            T value = sameIndex.computeIfAbsent(sameTypeRange.getSame(), k -> supplier.get());

            return Collections.singletonList(value);
        }

        if (range instanceof TypeRange.DownwardsTypeRange) {
            TypeRange.DownwardsTypeRange<?> downwardsTypeRange = (TypeRange.DownwardsTypeRange<?>) range;
            List<T> values = new ArrayList<>();

            for (Class<?> aClass : downwardsTypeRange.getInclude()) {
                T value = downwardIndex.computeIfAbsent(aClass, k -> supplier.get());
                values.add(value);
            }

            return values;
        }

        if (range instanceof TypeRange.UpwardsTypeRange) {
            TypeRange.UpwardsTypeRange<?> upwardsTypeRange = (TypeRange.UpwardsTypeRange<?>) range;
            T value = upwardIndex.computeIfAbsent(upwardsTypeRange.getType(), k -> supplier.get());

            return Collections.singletonList(value);
        }

        return Collections.emptyList();
    }

    private class TypeIterable implements Iterable<T> {

        private final Class<?> type;

        private TypeIterable(Class<?> type) {
            this.type = type;
        }

        @Override
        public Iterator<T> iterator() {
            return new TypeIterator(type);
        }

    }

    private class TypeIterator implements Iterator<T> {

        private final Class<?> type;
        private boolean sameType = true;
        private boolean downwardsType = true;
        private boolean upwardsType = true;
        private Iterator<Class<?>> upwardIterator;

        private T next;

        private TypeIterator(Class<?> type) {
            this.type = type;
            this.next();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            T next = this.next;
            this.next = null;

            if (sameType) {
                sameType = false;

                T t = sameIndex.get(type);

                if (t != null) {
                    this.next = t;
                    return next;
                }
            }

            if (downwardsType) {
                downwardsType = false;

                T t = downwardIndex.get(type);

                if (t != null) {
                    this.next = t;
                    return next;
                }
            }

            if (upwardsType) {
                upwardsType = false;
                upwardIterator = new IterableSuperClassResolver(type).iterator();
            }

            while (upwardIterator.hasNext()) {
                Class<?> nextType = upwardIterator.next();
                T t = upwardIndex.get(nextType);

                if (t != null) {
                    this.next = t;
                    return next;
                }
            }

            return next;
        }
    }

}
