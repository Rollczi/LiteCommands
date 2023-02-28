package dev.rollczi.litecommands.modern.meta;

import java.util.List;
import java.util.Set;

public interface CommandMetaType<T> {

    T cast(Object value);

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

    }

    class CommandMetaTypeSet<T> implements CommandMetaType<Set<T>> {

        @Override
        @SuppressWarnings("unchecked")
        public Set<T> cast(Object value) {
            return (Set<T>) value;
        }

    }

}
