package dev.rollczi.litecommands.event;

import dev.rollczi.litecommands.bind.BindRegistry;
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
    private final LookupMethodReflection lookupMethodReflection = new LookupMethodReflection();

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

            for (int i = 1; i < declaredMethod.getParameterCount(); i++) {
                bindClasses[i - 1] = declaredMethod.getParameterTypes()[i];
            }

            listeners.computeIfAbsent(firstEventParameter, key -> new HashSet<>()).add(new SubscriberMethod(listener, declaredMethod.getName(), bindClasses));
        }
    }

    private class SubscriberMethod {

        private final Object listener;
        private final String methodName;
        private final Class<?>[] bindClasses;

        public SubscriberMethod(Object listener, String methodName, Class<?>[] bindClasses) {
            this.listener = listener;
            this.methodName = methodName;
            this.bindClasses = bindClasses;
        }

        public void invoke(Event event) {
            Object[] binds = new Object[bindClasses.length + 1];
            binds[0] = event;

            for (int i = 1; i < binds.length; i++) {
                Result<?, String> result = bindRegistry.getInstance(bindClasses[i - 1]);

                if (result.isErr()) {
                    throw new IllegalArgumentException("Cannot bind " + bindClasses[i - 1].getName() + " for " + listener.getClass().getName() + "#" + methodName);
                }

                binds[i] = result.get();
            }

            lookupMethodReflection.invoke(listener, methodName, binds);
        }

    }

}
