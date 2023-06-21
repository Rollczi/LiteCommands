package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.scope.Scopeable;

import java.util.List;
import java.util.stream.Collectors;

public class ValidatorScope implements Scope {

    private final Class<?> type;

    private ValidatorScope(Class<?> type) {
        this.type = type;
    }

    @Override
    public boolean isApplicable(Scopeable scopeable) {
        List<Class<? extends Validator<?>>> validators = scopeable.metaCollector().collect(Meta.VALIDATORS).stream()
            .flatMap(classes -> classes.stream())
            .collect(Collectors.toList());


        for (Class<? extends Validator<?>> validator : validators) {
            if (validator.equals(this.type)) {
                return true;
            }
        }

        return false;
    }

    public static Scope of(Class<? extends Validator> type) {
        return new ValidatorScope(type);
    }

    Class<?> getType() {
        return type;
    }
}
