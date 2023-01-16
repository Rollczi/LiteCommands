package dev.rollczi.litecommands.modern.extension.annotated.inject;

import java.util.List;

public class InjectorException extends RuntimeException {

    public InjectorException() {
    }

    public InjectorException(String message) {
        super(message);
    }

    public InjectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InjectorException(Throwable cause) {
        super(cause);
    }

    public InjectorException(String message, List<InjectorException> exceptions) {
        super(message + " (" + exceptions.size() + " exceptions)");
        exceptions.forEach(this::addSuppressed);
    }
}
