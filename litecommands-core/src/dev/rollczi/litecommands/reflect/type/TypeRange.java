package dev.rollczi.litecommands.reflect.type;

import dev.rollczi.litecommands.reflect.ReflectUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface TypeRange<T> {

    Class<T> getBaseType();

    Set<Class<?>> getTypesInRange(Class<?> referenceType);

    boolean isInRange(Class<?> type);

    static <T> TypeRange<T> same(Class<T> type) {
        return new SameTypeRange<>(type);
    }

    static <T> TypeRange<T> upwards(Class<T> type) {
        return new UpwardsTypeRange<>(type);
    }

    static <T> TypeRange<T> downwards(Class<T> type) {
        return new DownwardsTypeRange<>(type, Collections.emptySet());
    }

    static <T> TypeRange<T> downwards(Class<T> type, Class<?>... exclude) {
        return new DownwardsTypeRange<>(type, new HashSet<>(Arrays.asList(exclude)));
    }

    static <T> TypeRange<T> downwards(Class<T> type, Collection<Class<?>> exclude) {
        return new DownwardsTypeRange<>(type, new HashSet<>(exclude));
    }

    class SameTypeRange<T> implements TypeRange<T> {

        private final Class<T> type;

        public SameTypeRange(Class<T> type) {
            this.type = type;
        }

        public Class<T> getSame() {
            return type;
        }

        @Override
        public Class<T> getBaseType() {
            return null;
        }

        @Override
        public Set<Class<?>> getTypesInRange(Class<?> referenceType) {
            if (type == referenceType) {
                return Collections.singleton(type);
            }

            return Collections.emptySet();
        }

        @Override
        public boolean isInRange(Class<?> type) {
            return this.type == type;
        }

    }

    class UpwardsTypeRange<T> implements TypeRange<T> {

        private final Class<T> type;

        public UpwardsTypeRange(Class<T> type) {
            this.type = type;
        }

        public Class<?> getType() {
            return type;
        }

        @Override
        public Class<T> getBaseType() {
            return type;
        }

        @Override
        public Set<Class<?>> getTypesInRange(Class<?> referenceType) {
            Set<Class<?>> types = new HashSet<>();

            for (Class<?> superType : ReflectUtil.getAllTypes(referenceType)) {
                if (type.isAssignableFrom(superType)) {
                    types.add(superType);
                }
            }

            return types;
        }

        @Override
        public boolean isInRange(Class<?> type) {
            return this.type.isAssignableFrom(type);
        }

    }

    class DownwardsTypeRange<T> implements TypeRange<T> {

        private final Class<T> type;
        private final Set<Class<?>> include;

        public DownwardsTypeRange(Class<T> type, Set<Class<?>> exclude) {
            this.type = type;
            this.include = includedDownwards(exclude, new HashSet<>(), type);
        }

        public Set<Class<?>> getInclude() {
            return include;
        }

        @Override
        public Class<T> getBaseType() {
            return type;
        }

        @Override
        public Set<Class<?>> getTypesInRange(Class<?> referenceType) {
            return Collections.unmodifiableSet(include);
        }

        @Override
        public boolean isInRange(Class<?> type) {
            return include.contains(type);
        }

        private static Set<Class<?>> includedDownwards(Set<Class<?>> exclude, Set<Class<?>> included, Class<?> type) {
            if (exclude.contains(type)) {
                return included;
            }

            included.add(type);

            Class<?> nextSuperType = type.getSuperclass();

            if (nextSuperType != null) {
                includedDownwards(exclude, included, nextSuperType);
            }

            for (Class<?> anInterface : type.getInterfaces()) {
                includedDownwards(exclude, included, anInterface);
            }

            return included;
        }

    }

}
