package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.parser.input.ParsableInputMatcher;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.input.InputMatcher;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.argument.suggestion.SuggestionResult;
import dev.rollczi.litecommands.argument.suggestion.SuggestionService;
import dev.rollczi.litecommands.argument.suggestion.input.SuggestionInput;
import dev.rollczi.litecommands.argument.suggestion.input.SuggestionInputMatcher;

import java.util.concurrent.CompletableFuture;

public class CommandManager<SENDER> {

    private final CommandRootRouteImpl<SENDER> root = new CommandRootRouteImpl<>();

    private final Platform<SENDER, ?> platform;

    private final CommandExecuteService<SENDER> executeService;
    private final SuggestionService<SENDER> suggestionService;

    public CommandManager(Platform<SENDER, ?> platform, CommandExecuteService<SENDER> executeService, SuggestionService<SENDER> suggestionService) {
        this.platform = platform;
        this.executeService = executeService;
        this.suggestionService = suggestionService;
    }

    public CommandRoute<SENDER> getRoot() {
        return this.root;
    }

    public void register(CommandRoute<SENDER> commandRoute) {
        PlatformListener listener = new PlatformListener(commandRoute);

        this.platform.register(commandRoute, listener);
        this.root.appendToRoot(commandRoute);
    }

    public void registerAll() {
        this.platform.start();
    }

    class PlatformListener implements PlatformInvocationListener<SENDER>, PlatformSuggestionListener<SENDER> {

        private final CommandRoute<SENDER> commandRoute;

        PlatformListener(CommandRoute<SENDER> commandRoute) {
            this.commandRoute = commandRoute;
        }

        @Override
        public CompletableFuture<CommandExecuteResult> execute(Invocation<SENDER> invocation, ParseableInput<?> arguments) {
            ParsableInputMatcher<?> matcher = arguments.createMatcher();
            CommandRoute<SENDER> commandRoute = findRoute(this.commandRoute, matcher);

            return executeService.execute(invocation, matcher, commandRoute);
        }

        @Override
        public SuggestionResult suggest(Invocation<SENDER> invocation, SuggestionInput<?> suggestion) {
            SuggestionInputMatcher<?> matcher = suggestion.createMatcher();
            CommandRoute<SENDER> find = findRoute(this.commandRoute, matcher);

            return suggestionService.suggest(invocation, matcher, find);
        }
    }

    private CommandRoute<SENDER> findRoute(CommandRoute<SENDER> command, InputMatcher matcher) {
        if (!matcher.hasNextRoute()) {
            return command;
        }

        // TODO Preformace, maybe use map in CommandRouteImpl
        for (CommandRoute<SENDER> child : command.getChildren()) {
            if (!child.isNameOrAlias(matcher.showNextRoute())) {
                continue;
            }

            matcher.nextRoute();
            return this.findRoute(child, matcher);
        }

        return command;
    }

    public void unregisterAll() {
        this.root.clearChildren();
        this.platform.unregisterAll();
        this.platform.stop();
    }

}
