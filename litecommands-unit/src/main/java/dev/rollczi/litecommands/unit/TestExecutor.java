package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.argument.PreparedArgumentResult;
import dev.rollczi.litecommands.command.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.command.CommandExecutorMatchResult;
import panda.std.Pair;

import java.util.Collections;
import java.util.List;

public class TestExecutor<SENDER> extends AbstractCommandExecutor<SENDER, PreparedArgument<SENDER, ?>> {

    public TestExecutor() {
        super(Collections.emptyList());
    }

    @Override
    protected CommandExecutorMatchResult match(List<Pair<PreparedArgument<SENDER, ?>, PreparedArgumentResult.Success<?>>> results) {
        return CommandExecutorMatchResult.success(() -> CommandExecuteResult.success(null, Void.class));
    }

}
