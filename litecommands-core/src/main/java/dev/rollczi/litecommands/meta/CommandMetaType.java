package dev.rollczi.litecommands.meta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface CommandMetaType<T> {

    T cast(Object value);

    default T in(T value) {
        return value;
    }

    default T out(T value) {
        return value;
    }

    static <E> CommandMetaType<List<E>> list() {
        return new CommandMetaTypeList<>();
    }

    static <E> CommandMetaType<Set<E>> set() {
        return new CommandMetaTypeSet<>();
    }

    static <T> CommandMetaType<T> of(Class<T> type) {
        return new CommandMetaTypeImpl<>(type);
    }

    class CommandMetaTypeImpl<T> implements CommandMetaType<T> {

        private final Class<T> type;

        public CommandMetaTypeImpl(Class<T> type) {
            this.type = type;
        }

        @Override
        public T cast(Object value) {
            return this.type.cast(value);
        }

    }

    class CommandMetaTypeList<T> implements CommandMetaType<List<T>> {

        @Override
        @SuppressWarnings("unchecked")
        public List<T> cast(Object value) {
            return (List<T>) value;
        }

        @Override
        public List<T> in(List<T> value) {
            return new ArrayList<>(value);
        }

        @Override
        public List<T> out(List<T> value) {
            return new ArrayList<>(value);
        }
    }

    class CommandMetaTypeSet<T> implements CommandMetaType<Set<T>> {

        @Override
        @SuppressWarnings("unchecked")
        public Set<T> cast(Object value) {
            return (Set<T>) value;
        }

        @Override
        public Set<T> in(Set<T> value) {
            return new HashSet<>(value);
        }

        @Override
        public Set<T> out(Set<T> value) {
            return new HashSet<>(value);
        }

    }

}
