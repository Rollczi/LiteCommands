package dev.rollczi.litecommands.injector;

import org.jetbrains.annotations.NotNull;

public class InjectCausedException extends InjectException {

    private final Throwable cause;

    public InjectCausedException(Throwable cause) {
        this.cause = cause;
    }

    public InjectCausedException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    @Override
    @NotNull
    public synchronized Throwable getCause() {
        return cause;
    }

}
