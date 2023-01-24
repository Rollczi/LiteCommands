package dev.rollczi.litecommands.modern.command.filter;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.invocation.Invocation;

import java.util.ArrayList;
import java.util.List;

public class CommandFilterService<SENDER> {

    private final List<CommandFilter<SENDER>> commandFilters = new ArrayList<>();

    public void registerFilter(CommandFilter<SENDER> commandFilter) {
        this.commandFilters.add(commandFilter);
    }

    public boolean isValid(Invocation<SENDER> invocation, CommandRoute commandRoute, CommandExecutor commandExecutor) {
        for (CommandFilter<SENDER> commandFilter : this.commandFilters) {
            if (!commandFilter.test(invocation, commandRoute, commandExecutor)) {
                return false;
            }
        }

        return true;
    }

}
