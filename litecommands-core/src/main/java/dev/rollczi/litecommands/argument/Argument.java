package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;

public interface Argument<SENDER, A extends Annotation> extends ParameterHandler {

    MatchResult match(LiteInvocation invocation, ArgumentContext<A> context);

    default List<Suggestion> suggestion(LiteInvocation invocation, Parameter parameter, A annotation) {
        return Collections.emptyList();
    }

    default boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return false;
    }

    default boolean isOptional() {
        return false;
    }

    default List<Object> defaultValue() {
        return Collections.emptyList();
    }

    default Class<?> getNativeClass() {
        return this.getClass();
    }

    default Option<String> getName(Parameter parameter, A annotation) {
        Name name = parameter.getAnnotation(Name.class);
        ArgumentName argumentName = this.getNativeClass().getAnnotation(ArgumentName.class);

        if (name != null) {
            return Option.of(name.value());
        }

        if (argumentName != null) {
            return Option.of(argumentName.value());
        }

        return Option.none();
    }

    default Option<String> getSchematic(Parameter parameter, A annotation) {
        return Option.none();
    }

}
