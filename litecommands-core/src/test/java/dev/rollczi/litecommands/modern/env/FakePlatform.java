package dev.rollczi.litecommands.modern.env;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.invocation.InvocationResult;
import dev.rollczi.litecommands.modern.platform.Platform;
import dev.rollczi.litecommands.modern.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.modern.platform.PlatformSuggestListener;

import java.util.LinkedHashMap;
import java.util.Map;

public class FakePlatform implements Platform<FakeSender> {

    private final Map<CommandRoute<FakeSender>, PlatformInvocationListener<FakeSender>> executeListeners = new LinkedHashMap<>();
    private final Map<CommandRoute<FakeSender>, PlatformSuggestListener<FakeSender>> suggestListeners = new LinkedHashMap<>();

    @Override
    public void registerExecuteListener(CommandRoute<FakeSender> commandRoute, PlatformInvocationListener<FakeSender> executeListener) {
        this.executeListeners.put(commandRoute, executeListener);
    }

    @Override
    public void registerSuggestionListener(CommandRoute<FakeSender> commandRoute, PlatformSuggestListener<FakeSender> suggestListener) {
        this.suggestListeners.put(commandRoute, suggestListener);
    }

    public InvocationResult<FakeSender> execute(String command) {
        FakeSender fakeSender = new FakeSender();
        FakePlatformSender fakePlatformSender = new FakePlatformSender(fakeSender);
        String label = command.split(" ")[0];
        String[] args = command.substring(label.length() + 1).split(" ");

        Invocation<FakeSender> invocation = new Invocation<>(fakeSender, fakePlatformSender, label, label, args);

        for (Map.Entry<CommandRoute<FakeSender>, PlatformInvocationListener<FakeSender>> entry : this.executeListeners.entrySet()) {
            if (entry.getKey().isNameOrAlias(label)) {
                return entry.getValue().execute(invocation);
            }
        }

        throw new IllegalStateException("No command found for " + command);
    }


    public InvocationResult<FakeSender> execute(String command, String... arguments) {
        FakeSender fakeSender = new FakeSender();
        FakePlatformSender fakePlatformSender = new FakePlatformSender(fakeSender);

        Invocation<FakeSender> invocation = new Invocation<>(fakeSender, fakePlatformSender, command, command, arguments);

        for (Map.Entry<CommandRoute<FakeSender>, PlatformInvocationListener<FakeSender>> entry : this.executeListeners.entrySet()) {
            if (entry.getKey().isNameOrAlias(command)) {
                return entry.getValue().execute(invocation);
            }
        }

        throw new IllegalStateException("No command found for " + command);
    }


    public void suggest(String command) {
        FakeSender fakeSender = new FakeSender();
        FakePlatformSender fakePlatformSender = new FakePlatformSender(fakeSender);
        String label = command.split(" ")[0];
        String[] args = command.substring(label.length()).split(" ");

        Invocation<FakeSender> invocation = new Invocation<>(fakeSender, fakePlatformSender, label, label, args);




    }

}
