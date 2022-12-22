package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;

public class ArgumentResultContext<DETERMINANT, EXPECTED> {

    private final ArgumentResult<EXPECTED> result;
    private final ArgumentContext<DETERMINANT, EXPECTED> context;

    public ArgumentResultContext(ArgumentResult<EXPECTED> result, ArgumentContext<DETERMINANT, EXPECTED> context) {
        this.result = result;
        this.context = context;
    }

    public ArgumentResult<EXPECTED> getResult() {
        return result;
    }

    public ArgumentContext<DETERMINANT, EXPECTED> getContext() {
        return context;
    }

}

