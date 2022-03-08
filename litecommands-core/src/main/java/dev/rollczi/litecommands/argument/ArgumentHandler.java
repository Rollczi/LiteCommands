package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import panda.std.Option;

import java.util.List;

public interface ArgumentHandler<T> {

    T parse(LiteComponent.ContextOfResolving context, int rawIndex) throws ValidationCommandException;

    List<String> tabulation(LiteComponent.ContextOfResolving context);

    default String getName() {
        return Option.of(this.getClass().getAnnotation(ArgumentName.class))
                .map(ArgumentName::value)
                .orThrow(() -> new IllegalStateException("annotation @ArgumentName is not found before class " + this.getNativeClass().getName()));
    }

    default Class<? extends ArgumentHandler> getNativeClass() {
        return this.getClass();
    }

    default boolean isValid(LiteComponent.ContextOfResolving context, String argument) {
        List<String> tabulations = tabulation(context);

        for (String tab : tabulations) {
            if (tab.equalsIgnoreCase(argument)) {
                return true;
            }
        }

        return false;
    }

}
