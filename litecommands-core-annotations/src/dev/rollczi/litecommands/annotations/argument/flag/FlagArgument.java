package dev.rollczi.litecommands.annotations.argument.flag;

import dev.rollczi.litecommands.annotations.argument.ParameterArgument;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.reflect.Parameter;
import java.util.Optional;

class FlagArgument extends ParameterArgument<Flag, Boolean> {

    FlagArgument(WrapperRegistry wrapperRegistry, Parameter parameter, Flag annotation) {
        super(wrapperRegistry, parameter, annotation);
    }

    @Override
    public String getName() {
        return getAnnotation().value();
    }

    @Override
    public Optional<Boolean> defaultValue() {
        return Optional.of(Boolean.FALSE);
    }

}
