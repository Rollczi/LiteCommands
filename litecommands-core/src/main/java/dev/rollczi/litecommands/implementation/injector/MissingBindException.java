package dev.rollczi.litecommands.implementation.injector;

import dev.rollczi.litecommands.shared.ReflectFormat;

import java.util.List;
import java.util.stream.Collectors;

final class MissingBindException extends ReflectiveOperationException {
    private final List<Class<?>> missing;

    MissingBindException(List<Class<?>> missing) {
        this.missing = missing;
    }

    @Override
    public String getMessage() {
        return "Cannot find binds for " + missing.stream().map(ReflectFormat::singleClass)
                .map(text -> "[" + text + "]")
                .collect(Collectors.joining(", "));
    }

    public List<Class<?>> getMissing() {
        return missing;
    }
}
