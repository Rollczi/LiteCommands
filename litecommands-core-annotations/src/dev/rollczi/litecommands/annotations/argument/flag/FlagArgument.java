package dev.rollczi.litecommands.annotations.argument.flag;

import dev.rollczi.litecommands.annotations.argument.ParameterArgument;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.reflect.Parameter;

class FlagArgument<EXPECTED> extends ParameterArgument<Flag, EXPECTED> {

    FlagArgument(WrapperRegistry wrapperRegistry, Parameter parameter, Flag annotation) {
        super(wrapperRegistry, parameter, annotation);
    }

}
