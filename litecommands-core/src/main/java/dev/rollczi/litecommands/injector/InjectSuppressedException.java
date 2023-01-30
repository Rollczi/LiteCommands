package dev.rollczi.litecommands.injector;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InjectSuppressedException extends InjectException {

    private final List<Throwable> causes;

    public InjectSuppressedException(List<? extends Throwable> causes) {
        this.causes = new ArrayList<>(causes);

        for (Throwable cause : causes) {
            this.addSuppressed(cause);
        }
    }

    public InjectSuppressedException(String message, List<? extends Throwable> causes) {
        super(message);
        this.causes = new ArrayList<>(causes);

        for (Throwable cause : causes) {
            this.addSuppressed(cause);
        }
    }

    @NotNull
    public List<Throwable> getCauses() {
        return causes;
    }

}
