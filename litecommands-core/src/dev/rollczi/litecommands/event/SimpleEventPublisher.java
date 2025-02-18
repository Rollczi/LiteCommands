package dev.rollczi.litecommands.event;

import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.bind.BindResult;
import dev.rollczi.litecommands.reflect.IterableSuperClassResolver;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;
import dev.rollczi.litecommands.reflect.ReflectUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class SimpleEventPublisher implements EventPublisher {

    private final Map<Class<?>, Set<Consumer<?>>> listeners = new HashMap<>();
    private final Map<Class<?>, Set<Class<?>>> superTypesCache = new HashMap<>();
    private final BindRegistry bindRegistry;

    public SimpleEventPublisher(BindRegistry bindRegistry) {
        this.bindRegistry = bindRegistry;
    }

    @Override
    public boolean hasSubscribers(Class<? extends Event> eventClass) {
        for (Class<?> type : extractSuperTypes(eventClass)) {
            if (listeners.containsKey(eventClass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <E extends Event> E publish(E event) {
        for (Class<?> type : extractSuperTypes(event.getClass())) {
            Set<Consumer<?>> listeners = this.listeners.get(type);

            if (listeners == null) {
                continue;
            }

            for (Consumer<?> listener : listeners) {
                ((Consumer<E>) listener).accept(event);
            }
        }

        return event;
    }

    @Override
    public void subscribe(EventListener listener) {
        Class<?> listenerClass = listener.getClass();

        for (Method declaredMethod : listenerClass.getDeclaredMethods()) {
            Subscriber annotation = declaredMethod.getAnnotation(Subscriber.class);
            if (annotation == null) {
                continue;
            }

            if (declaredMethod.getParameterCount() == 0) {
                throw new IllegalArgumentException("Method " + declaredMethod.getName() + " in " + listenerClass.getName() + " must have at least one parameter");
            }

            Class<?> firstEventParameter = declaredMethod.getParameterTypes()[0];

            if (!Event.class.isAssignableFrom(firstEventParameter)) {
                throw new IllegalArgumentException("First parameter in method " + declaredMethod.getName() + " in " + listenerClass.getName() + " must be a subclass of Event");
            }

            if (firstEventParameter.isInterface()) {
                throw new IllegalArgumentException("First parameter in method " + declaredMethod.getName() + " in " + listenerClass.getName() + " cannot be an interface");
            }

            Class<?>[] bindClasses = new Class[declaredMethod.getParameterCount() - 1];
            System.arraycopy(declaredMethod.getParameterTypes(), 1, bindClasses, 0, declaredMethod.getParameterCount() - 1);

            declaredMethod.setAccessible(true);
            listeners.computeIfAbsent(firstEventParameter, key -> new HashSet<>()).add(new SubscriberMethod(listener, declaredMethod, bindClasses));
        }
    }

    @Override
    public <E extends Event> void subscribe(Class<E> event, Consumer<E> listener) {
        listeners.computeIfAbsent(event, key -> new HashSet<>()).add(listener);
    }

    private Iterable<Class<?>> extractSuperTypes(Class<?> baseType) {
        Set<Class<?>> cached = superTypesCache.get(baseType);
        if (cached != null) {
            return cached;
        }

        Set<Class<?>> superTypes = new HashSet<>();
        for (Class<?> type : new IterableSuperClassResolver(baseType)) {
            if (Event.class.isAssignableFrom(type)) {
                superTypes.add(type);
            }
        }
        superTypesCache.put(baseType, superTypes);
        return superTypes;
    }

    private class SubscriberMethod implements Consumer<Event> {

        private final EventListener listener;
        private final Method declaredMethod;
        private final Class<?>[] bindClasses;

        public SubscriberMethod(EventListener listener, Method declaredMethod, Class<?>[] bindClasses) {
            this.listener = listener;
            this.declaredMethod = declaredMethod;
            this.bindClasses = bindClasses;
        }

        @Override
        public void accept(Event event) {
            Object[] args = new Object[bindClasses.length + 1];
            args[0] = event;

            for (int i = 1; i < args.length; i++) {
                BindResult<?> result = bindRegistry.getInstance(bindClasses[i - 1]);

                if (result.isError()) {
                    throw new IllegalArgumentException("Cannot bind " + bindClasses[i - 1].getName() + " for " + listener.getClass().getName() + "#" + declaredMethod.getName());
                }

                args[i] = result.getSuccess();
            }

            try {
                declaredMethod.invoke(listener, args);
            }
            catch (IllegalAccessException exception) {
                throw new LiteCommandsReflectInvocationException(declaredMethod, "Cannot access method", exception);
            }
            catch (InvocationTargetException exception) {
                throw new LiteCommandsReflectInvocationException(declaredMethod, "Cannot invoke method", exception.getCause());
            }
        }

    }

}
