package dev.rollczi.litecommands.event;

import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class LookupMethodReflection {

    private final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private final Map<MethodTypeKey, MethodType> METHOD_TYPE_CACHE = new HashMap<>();
    private final Map<ReturnMethodKey, Class<?>> RETURN_TYPE_CACHE = new HashMap<>();
    private final Map<MethodKey, MethodHandle> METHOD_HANDLE_CACHE = new HashMap<>();

    Object invoke(Object instance, String name, Object... args) {
        try {
            Object[] newArgs = new Object[args.length + 1];

            newArgs[0] = instance;
            System.arraycopy(args, 0, newArgs, 1, args.length);

            return getMethodHandle(instance.getClass(), name, args).invokeWithArguments(newArgs);
        } catch (Throwable exception) {
            throw new LiteCommandsReflectException("Cannot invoke method for " + instance.getClass().getName() + "#" + name, exception);
        }
    }

    private MethodHandle getMethodHandle(Class<?> classType, String name, Object... args) {
        Class<?>[] argClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argClasses[i] = args[i].getClass();
        }

        MethodKey methodKey = new MethodKey(classType, name, argClasses);
        MethodHandle methodHandle = METHOD_HANDLE_CACHE.get(methodKey);

        if (methodHandle != null) {
            return methodHandle;
        }

        Class<?> returnType = getReturnType(classType, name, argClasses);
        MethodType methodType = METHOD_TYPE_CACHE.computeIfAbsent(new MethodTypeKey(returnType, argClasses), key -> MethodType.methodType(returnType, argClasses));

        try {
            MethodHandle virtual = LOOKUP.findVirtual(classType, name, methodType);
            METHOD_HANDLE_CACHE.put(methodKey, virtual);
            return virtual;
        } catch (NoSuchMethodException | IllegalAccessException exception) {
            throw new LiteCommandsReflectException("Cannot find method handle for " + classType.getName() + "#" + name, exception);
        }
    }

    private Class<?> getReturnType(Class<?> classType, String name, Class<?>... args) {
        return RETURN_TYPE_CACHE.computeIfAbsent(new ReturnMethodKey(classType, name, args), key -> getReturnType0(classType, name, args));
    }

    private Class<?> getReturnType0(Class<?> classType, String name, Class<?>... args) {
        try {
            return classType.getDeclaredMethod(name, args).getReturnType();
        } catch (NoSuchMethodException exception) {
            throw new LiteCommandsReflectException("Cannot find method for " + classType.getName() + "#" + name, exception);
        }
    }

    private static class ReturnMethodKey {
        private final Class<?> classType;
        private final String name;
        private final Class<?>[] args;

        private ReturnMethodKey(Class<?> classType, String name, Class<?>[] args) {
            this.classType = classType;
            this.name = name;
            this.args = args;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReturnMethodKey that = (ReturnMethodKey) o;
            return Objects.equals(classType, that.classType) && Objects.equals(name, that.name) && Objects.deepEquals(args, that.args);
        }

        @Override
        public int hashCode() {
            return Objects.hash(classType, name, Arrays.hashCode(args));
        }
    }

    private static class MethodTypeKey {
        private final Class<?> returnType;
        private final Class<?>[] args;

        public MethodTypeKey(Class<?> returnType, Class<?>[] args) {
            this.returnType = returnType;
            this.args = args;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodTypeKey methodTypeKey = (MethodTypeKey) o;
            return Objects.equals(returnType, methodTypeKey.returnType) && Objects.deepEquals(args, methodTypeKey.args);
        }

        @Override
        public int hashCode() {
            return Objects.hash(returnType, Arrays.hashCode(args));
        }
    }

    private static class MethodKey {
        private final Class<?> classType;
        private final String name;
        private final Class<?>[] args;

        private MethodKey(Class<?> classType, String name, Class<?>[] args) {
            this.classType = classType;
            this.name = name;
            this.args = args;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodKey methodKey = (MethodKey) o;
            return Objects.equals(classType, methodKey.classType) && Objects.equals(name, methodKey.name) && Objects.deepEquals(args, methodKey.args);
        }

        @Override
        public int hashCode() {
            return Objects.hash(classType, name, Arrays.hashCode(args));
        }
    }

}