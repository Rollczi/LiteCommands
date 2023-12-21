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
        private Queue<Class<?>> interfaces;
        private Set<Class<?>> visitedInterfaces;
        private boolean interfacesDone = false;


        private TypeIterator() {
            this.next = baseType;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }


        @Override
        public Class<?> next() {
            Class<?> next = this.next;
            this.next = null;

            if (next == null) {
                return null;
            }

            Class<?> superclass = next.getSuperclass();

            if (superclass != null) {
                this.next = superclass;
                return next;
            }

            if (!interfacesDone) {
                interfacesDone = true;
                interfaces = new LinkedList<>();
                interfaces.addAll(ReflectIndex.getInterfaces(baseType));
                visitedInterfaces = new HashSet<>();
            }

            while (!interfaces.isEmpty()) {
                Class<?> nextInterface = interfaces.poll();

                if (visitedInterfaces.contains(nextInterface)) {
                    continue;
                }

                visitedInterfaces.add(nextInterface);
                interfaces.addAll(ReflectIndex.getInterfaces(nextInterface));

                this.next = nextInterface;
                return next;
            }

            return next;
        }

    }

}
