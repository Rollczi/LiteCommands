package dev.rollczi.litecommands.injector;

import java.util.List;
import java.util.stream.Collectors;

public class MissingBindException extends InjectException {

    private final List<Class<?>> missing;
    private final String message;

    public MissingBindException(List<Class<?>> missing, String message) {
        super(message);
        this.missing = missing;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return (message != null ? message + " " : "Cannot find binds for ") + "[" + missing.stream().map(Class::getName).collect(Collectors.joining(", ")) + "]";
    }

}
