package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.component.LiteComponent;

public class InjectUtils {

    private InjectUtils() {}

    public static LiteComponent.ContextOfResolving getContextFromObjects(Object[] objects) {
        return (LiteComponent.ContextOfResolving) objects[0];
    }

}
