package dev.rollczi.litecommands.utils;

import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.component.MethodExecutor;

public class InjectUtils {

    private InjectUtils() {}

    public static Object[] prepareInjectorArgs(LiteComponent.ContextOfResolving context, MethodExecutor executor) {
        return new Object[] { context, executor };
    }

    public static LiteComponent.ContextOfResolving getContextFromInjectorArgs(Object[] objects) {
        return (LiteComponent.ContextOfResolving) objects[0];
    }

    public static MethodExecutor getMethodExecutorFromInjectorArgs(Object[] objects) {
        return (MethodExecutor) objects[1];
    }

}
