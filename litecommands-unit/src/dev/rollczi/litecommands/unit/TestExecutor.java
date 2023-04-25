package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.command.requirements.CommandRequirement;
import dev.rollczi.litecommands.command.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.CommandExecutorMatchResult;

import java.util.Collections;
import java.util.List;

public class TestExecutor<SENDER> extends AbstractCommandExecutor<SENDER, CommandRequirement<SENDER, ?>> {

    public TestExecutor() {
        super(Collections.emptyList());
    }

    @Override
    protected CommandExecutorMatchResult match(List<Match<CommandRequirement<SENDER, ?>>> results) {
        return null;
    }

}
