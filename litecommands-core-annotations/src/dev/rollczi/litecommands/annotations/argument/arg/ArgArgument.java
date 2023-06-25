package dev.rollczi.litecommands.annotations.argument.arg;

import dev.rollczi.litecommands.annotations.argument.ParameterArgument;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.reflect.Parameter;

class ArgArgument<EXPECTED> extends ParameterArgument<Arg, EXPECTED> {

    protected ArgArgument(WrapperRegistry wrapperRegistry, Parameter parameter, Arg annotation) {
        super(wrapperRegistry, parameter, annotation);
    }

    @Override
    public String getName() {
        String value = this.getAnnotation().value();

        if (!value.isEmpty()) {
            return value;
        }

        return super.getName();
    }

}
