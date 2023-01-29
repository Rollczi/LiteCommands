package dev.rollczi.litecommands.modern.env;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.platform.Platform;
import dev.rollczi.litecommands.modern.platform.PlatformExecuteListener;
import dev.rollczi.litecommands.modern.platform.PlatformSuggestListener;

import java.util.LinkedHashMap;
import java.util.Map;

public class FakePlatform implements Platform<FakeSender> {

    private final Map<CommandRoute, PlatformExecuteListener<FakeSender>> executeListeners = new LinkedHashMap<>();
    private final Map<CommandRoute, PlatformSuggestListener<FakeSender>> suggestListeners = new LinkedHashMap<>();

    @Override
    public void registerExecuteListener(CommandRoute commandRoute, PlatformExecuteListener<FakeSender> executeListener) {
        this.executeListeners.put(commandRoute, executeListener);
    }

    @Override
    public void registerSuggestionListener(CommandRoute commandRoute, PlatformSuggestListener<FakeSender> suggestListener) {
        this.suggestListeners.put(commandRoute, suggestListener);
    }

    public void execute(String command) {
        FakeSender fakeSender = new FakeSender();
        FakePlatformSender fakePlatformSender = new FakePlatformSender(fakeSender);
        String label = command.split(" ")[0];
        String[] args = command.substring(label.length()).split(" ");

        Invocation<FakeSender> invocation = new Invocation<>(fakeSender, fakePlatformSender, label, label, args);

        for (Map.Entry<CommandRoute, PlatformExecuteListener<FakeSender>> entry : this.executeListeners.entrySet()) {
            if (entry.getKey().isNameOrAlias(label)) {
                return entry.getValue().execute(invocation);
            }
        }
    }

    public void suggest(String command) {
        FakeSender fakeSender = new FakeSender();
        FakePlatformSender fakePlatformSender = new FakePlatformSender(fakeSender);
        String label = command.split(" ")[0];
        String[] args = command.substring(label.length()).split(" ");

        Invocation<FakeSender> invocation = new Invocation<>(fakeSender, fakePlatformSender, label, label, args);

        this.suggestListeners.forEach(listener -> listener.execute(invocation));
    }

}
