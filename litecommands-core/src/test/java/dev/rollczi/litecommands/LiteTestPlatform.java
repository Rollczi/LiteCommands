package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.platform.Executor;
import dev.rollczi.litecommands.platform.LiteAbstractPlatformManager;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.litecommands.platform.Suggester;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import dev.rollczi.litecommands.valid.ValidationInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

    public ExecutionResult invocation(LiteTestSender liteSender, String command, String[] args) {
        LiteTestCommand liteTestCommand = commands.get(command);

        if (liteTestCommand == null) {
            LiteInvocation liteInvocation = new LiteInvocation(command, command, liteSenderCreator.create(null), args);
            LiteComponent.ContextOfResolving context = LiteComponent.ContextOfResolving.create(liteInvocation);

            return ExecutionResult.invalid(ValidationInfo.COMMAND_NO_EXIST, context);
        }

        return liteTestCommand.execute(liteSender, liteTestCommand.getScope().getName(), command, args);
    }

    public ExecutionResult invocation(String command, String[] args) {
        return invocation(new LiteTestSender(), command, args);
    }

    public List<String> suggestion(LiteTestSender liteSender, String command, String[] args) {
        LiteTestCommand liteTestCommand = commands.get(command);

        if (liteTestCommand == null) {
            return Collections.emptyList();
        }

        return liteTestCommand.suggest(liteSender, liteTestCommand.getScope().getName(), command, args);
    }
    public List<String> suggestion(String command, String[] args) {
        return suggestion(new LiteTestSender(), command, args);
    }

}
