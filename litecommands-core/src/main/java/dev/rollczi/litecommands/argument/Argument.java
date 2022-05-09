package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.Completion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface Argument<A extends Annotation> {

    MatchResult match(LiteInvocation invocation, A annotation, int currentRoute, int currentArgument);

    default boolean isRequired() {
        return true;
    }

    default List<Completion> complete(LiteInvocation invocation, A annotation) {
        return Collections.emptyList();
    }

    default List<Object> getDefaultValue() {
        return Collections.emptyList();
    }

    default Optional<String> getName(A annotation) {
        return Optional.ofNullable(this.getClass().getAnnotation(ArgumentName.class))
                .map(ArgumentName::value);
    }

    default Optional<String> getScheme(A annotation) {
        return Optional.empty();
    }

}
