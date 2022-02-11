package dev.rollczi.litecommands;

import dev.rollczi.litecommands.platform.Executor;
import dev.rollczi.litecommands.platform.LiteAbstractPlatformManager;
import dev.rollczi.litecommands.platform.Suggester;
import dev.rollczi.litecommands.scope.ScopeMetaData;

import java.util.HashMap;
import java.util.Map;

public class LiteTestPlatform extends LiteAbstractPlatformManager<Void> {

    private final Map<String, LiteTestCommand> commands = new HashMap<>();

    public LiteTestPlatform() {
        super(none -> new LiteTestSender());
    }

    @Override
    public void registerCommand(ScopeMetaData scope, Executor execute, Suggester suggester) {
        LiteTestCommand liteTestCommand = new LiteTestCommand(scope, execute, suggester, this.liteSenderCreator);

        for (String alias : scope.getAliases()) {
            commands.put(alias, liteTestCommand);
        }

        commands.put(scope.getName(), liteTestCommand);
    }

    @Override
    public void unregisterCommands() {
        commands.clear();
    }

    public void invocation(String command, String[] args) {
        LiteTestCommand liteTestCommand = commands.get(command);

        if (liteTestCommand == null) {
            return;
        }

        liteTestCommand.execute(liteTestCommand.getScope().getName(), command, args);
    }

}
