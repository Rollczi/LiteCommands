package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.Completion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

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

}
