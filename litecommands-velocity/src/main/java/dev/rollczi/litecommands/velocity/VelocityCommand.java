package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.platform.SuggestionListener;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.shared.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class VelocityCommand implements SimpleCommand {

    private final String name;
    private final ExecuteListener<CommandSource> executeListener;
    private final SuggestionListener<CommandSource> suggestionListener;

    public VelocityCommand(String name, ExecuteListener<CommandSource> executeListener, SuggestionListener<CommandSource> suggestionListener) {
        this.name = name;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;
    }

    @Override
    public void execute(Invocation invocation) {
        this.executeListener.execute(invocation.source(), this.convert(invocation, false));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return this.suggestionListener.suggest(invocation.source(), this.convert(invocation, true)).multilevelSuggestions();
    }

    private LiteInvocation convert(Invocation invocation, boolean suggest) {
        List<String> arguments = new ArrayList<>(Arrays.asList(invocation.arguments()));

        if (suggest && arguments.isEmpty()) {
            arguments.add(StringUtils.EMPTY);
        }

        return new LiteInvocation(new VelocitySender(invocation.source()), this.name, invocation.alias(), arguments.toArray(new String[0]));
    }

}
