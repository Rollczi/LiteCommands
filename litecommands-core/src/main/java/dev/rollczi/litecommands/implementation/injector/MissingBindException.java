package dev.rollczi.litecommands.implementation.injector;

import dev.rollczi.litecommands.shared.ReflectFormat;

import java.util.List;
import java.util.stream.Collectors;

final class MissingBindException extends ReflectiveOperationException {
    private final List<Class<?>> missing;
    private final String message;

    MissingBindException(List<Class<?>> missing) {
        this.missing = missing;
        this.message = null;
    }

    MissingBindException(List<Class<?>> missing, String message) {
        this.missing = missing;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return (message != null ? message + " " : "Cannot find binds for ") + missing.stream().map(ReflectFormat::singleClass).collect(Collectors.joining(", "));
    }

}
