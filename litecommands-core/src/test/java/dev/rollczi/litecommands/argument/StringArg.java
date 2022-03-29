package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.Collections;
import java.util.List;

@ArgumentName("text")
public class StringArg implements SingleArgumentHandler<String> {

    @Override
    public String parse(LiteInvocation invocation, String argument) throws ValidationCommandException {
        return argument;
    }

    @Override
    public List<String> tabulation(LiteInvocation invocation, String command, String[] args) {
        return Collections.emptyList();
    }

}
