package dev.rollczi.litecommands.modern.test.env;

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
    public void listenExecute(CommandRoute<FakeSender> commandRoute, PlatformInvocationListener<FakeSender> executeListener) {
        this.executeListeners.put(commandRoute, executeListener);
    }

    @Override
    public void listenSuggestion(CommandRoute<FakeSender> commandRoute, PlatformSuggestListener<FakeSender> suggestListener) {
        this.suggestListeners.put(commandRoute, suggestListener);
    }

    @Override
    public void unregisterAll() {
        this.executeListeners.clear();
        this.suggestListeners.clear();
    }

    public AssertExecute execute(String command) {
        String label = command.split(" ")[0];
        String[] args = command.length() > label.length()
            ? command.substring(label.length() + 1).split(" ")
            : new String[0];

        return execute(label, args);
    }

    public AssertExecute execute(String command, String... arguments) {
        FakeSender fakeSender = new FakeSender();
        FakePlatformSender fakePlatformSender = new FakePlatformSender();

        Invocation<FakeSender> invocation = new Invocation<>(fakeSender, fakePlatformSender, command, command, arguments);

        for (Map.Entry<CommandRoute<FakeSender>, PlatformInvocationListener<FakeSender>> entry : this.executeListeners.entrySet()) {
            if (entry.getKey().isNameOrAlias(command)) {
                PlatformInvocationListener<FakeSender> listener = entry.getValue();
                InvocationResult<FakeSender> result = listener.execute(invocation);

                return new AssertExecute(result);
            }
        }

        throw new IllegalStateException("No command found for " + command);
    }


    public void suggest(String command) {
        FakeSender fakeSender = new FakeSender();
        FakePlatformSender fakePlatformSender = new FakePlatformSender();
        String label = command.split(" ")[0];
        String[] args = command.substring(label.length()).split(" ");

        Invocation<FakeSender> invocation = new Invocation<>(fakeSender, fakePlatformSender, label, label, args);




    }

}
