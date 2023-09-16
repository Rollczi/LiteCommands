package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.annotation.AnnotationHolder;

public interface Requirement<SENDER, PARSED> extends MetaHolder {

    String getName();

    AnnotationHolder<?, PARSED, ?> getAnnotationHolder();

    <MATCHER extends ParseableInputMatcher<MATCHER>> RequirementResult<PARSED> match(
        Invocation<SENDER> invocation,
        MATCHER matcher
    );

}
