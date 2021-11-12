package dev.rollczi.litecommands.inject;

public class InjectUtils {

    private InjectUtils() {}

    public static InjectContext getContextFromObjects(Object[] objects) {
        return (InjectContext) objects[0];
    }

}
