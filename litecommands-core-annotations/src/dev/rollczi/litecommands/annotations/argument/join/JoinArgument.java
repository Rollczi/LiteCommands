package dev.rollczi.litecommands.annotations.argument.join;

import dev.rollczi.litecommands.annotations.argument.ParameterArgument;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.reflect.Parameter;

class JoinArgument<EXPECTED> extends ParameterArgument<Join, EXPECTED> {

    protected JoinArgument(WrapperRegistry wrapperRegistry, Parameter parameter, Join annotation) {
        super(wrapperRegistry, parameter, annotation);
    }

}
