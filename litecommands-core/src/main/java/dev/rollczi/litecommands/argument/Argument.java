package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.Suggestion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface Argument<A extends Annotation> extends ParameterHandler {

    MatchResult match(LiteInvocation invocation, Parameter parameter, A annotation, int currentRoute, int currentArgument);

    default List<Suggestion> complete(LiteInvocation invocation, Parameter parameter, A annotation) {
        return Collections.emptyList();
    }

    default boolean isOptional() {
        return false;
    }

    default List<Object> getDefault() {
        return Collections.emptyList();
    }

    default Class<?> getNativeClass() {
        return this.getClass();
    }

    default Optional<String> getName(A annotation) {
        return Optional.ofNullable(this.getNativeClass().getAnnotation(ArgumentName.class))
                .map(ArgumentName::value);
    }

    default Optional<String> getSchematic(A annotation) {
        return Optional.empty();
    }

}
