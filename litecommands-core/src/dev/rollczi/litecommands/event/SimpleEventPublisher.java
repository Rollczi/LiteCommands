package dev.rollczi.litecommands.event;

import dev.rollczi.litecommands.bind.BindRegistry;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.ApiStatus;
import panda.std.Result;

@ApiStatus.Experimental
public class SimpleEventPublisher implements EventPublisher {

    private final Map<Class<?>, Set<SubscriberMethod>> listeners = new HashMap<>();
    private final BindRegistry bindRegistry;

    public SimpleEventPublisher(BindRegistry bindRegistry) {
        this.bindRegistry = bindRegistry;
    }

    @Override
    public boolean hasSubscribers(Class<? extends Event> eventClass) {
        return listeners.containsKey(eventClass);
    }

    @Override
    public <E extends Event> E publish(E event) {
        Set<SubscriberMethod> methods = listeners.get(event.getClass());

        if (methods == null) {
            return event;
        }

        for (SubscriberMethod method : methods) {
            method.invoke(event);
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

    private class SubscriberMethod {

        private final EventListener listener;
        private final Method declaredMethod;
        private final Class<?>[] bindClasses;

        public SubscriberMethod(EventListener listener, Method declaredMethod, Class<?>[] bindClasses) {
            this.listener = listener;
            this.declaredMethod = declaredMethod;
            this.bindClasses = bindClasses;
        }

        public void invoke(Event event) {
            Object[] args = new Object[bindClasses.length + 1];
            args[0] = event;

            for (int i = 1; i < args.length; i++) {
                Result<?, String> result = bindRegistry.getInstance(bindClasses[i - 1]);

                if (result.isErr()) {
                    throw new IllegalArgumentException("Cannot bind " + bindClasses[i - 1].getName() + " for " + listener.getClass().getName() + "#" + declaredMethod.getName());
                }

                args[i] = result.get();
            }

            try {
                declaredMethod.invoke(listener, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
