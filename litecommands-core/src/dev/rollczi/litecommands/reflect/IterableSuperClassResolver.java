package dev.rollczi.litecommands.reflect;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class IterableSuperClassResolver implements Iterable<Class<?>> {

    private final Class<?> baseType;

    public IterableSuperClassResolver(Class<?> baseType) {
        this.baseType = baseType;
    }

    @NotNull
    @Override
    public Iterator<Class<?>> iterator() {
        return new TypeIterator();
    }

    private class TypeIterator implements Iterator<Class<?>> {

        private Class<?> next;
        private final Queue<Class<?>> interfaces = new LinkedList<>();
        private final Set<Class<?>> visitedInterfaces = new HashSet<>();

        private TypeIterator() {
            this.next = baseType;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }


        @Override
        public Class<?> next() {
            Class<?> nextToReturn = this.next;
            this.next = null;

            if (nextToReturn == null) {
                return null;
            }

            while (!interfaces.isEmpty()) {
                Class<?> nextInterface = interfaces.poll();

                if (visitedInterfaces.contains(nextInterface)) {
                    continue;
                }

                visitedInterfaces.add(nextInterface);
                interfaces.addAll(ReflectIndex.getInterfaces(nextInterface));

                this.next = nextInterface;
                return nextToReturn;
            }

            Class<?> superclass = nextToReturn.getSuperclass();

            if (superclass != null) {
                this.next = superclass;
                interfaces.addAll(ReflectIndex.getInterfaces(superclass));
                return nextToReturn;
            }

            if (this.next == null && nextToReturn != Object.class) {
                this.next = Object.class;
            }

            return nextToReturn;
        }

    }

}
