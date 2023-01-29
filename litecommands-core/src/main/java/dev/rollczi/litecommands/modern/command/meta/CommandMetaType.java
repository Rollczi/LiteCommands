package dev.rollczi.litecommands.modern.command.meta;

import java.util.ArrayList;

public interface CommandMetaType<T> {

    Class<T> getType();

    static <E> CommandMetaType<ArrayList<E>> list(Class<E> type) {
        return new CommandMetaTypeList<>();
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
        public Class<T> getType() {
            return this.type;
        }

    }

    class CommandMetaTypeList<T> implements CommandMetaType<ArrayList<T>> {

        private final Class<ArrayList<T>> type;

        public CommandMetaTypeList() {
            ArrayList<T> list = new ArrayList<>();

            this.type = (Class<ArrayList<T>>) list.getClass();
        }

        @Override
        public Class<ArrayList<T>> getType() {
            return this.type;
        }
    }

}
