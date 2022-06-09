package dev.rollczi.litecommands.argument;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ArgumentContext<A extends Annotation> {

    private final Parameter parameter;
    private final A annotation;
    private final int currentRoute;
    private final int currentArgument;

    public ArgumentContext(Parameter parameter, A annotation, int currentRoute, int currentArgument) {
        this.parameter = parameter;
        this.annotation = annotation;
        this.currentRoute = currentRoute;
        this.currentArgument = currentArgument;
    }

    public Parameter parameter() {
        return parameter;
    }

    public A annotation() {
        return annotation;
    }

    public int currentArgument() {
        return currentArgument;
    }

    public int currentRoute() {
        return currentRoute;
    }

}
