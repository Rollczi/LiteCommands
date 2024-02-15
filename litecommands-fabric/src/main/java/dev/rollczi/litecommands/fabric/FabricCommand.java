package dev.rollczi.litecommands.fabric;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minecraft.server.command.ServerCommandSource;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FabricCommand {
    private final CommandRoute<ServerCommandSource> baseRoute;
    private final PlatformInvocationListener<ServerCommandSource> invocationHook;
    private final PlatformSuggestionListener<ServerCommandSource> suggestionHook;
    private final UUID routeUUID;

    public FabricCommand(CommandRoute<ServerCommandSource> baseRoute, PlatformInvocationListener<ServerCommandSource> invocationHook, PlatformSuggestionListener<ServerCommandSource> suggestionHook) {
        this.baseRoute = baseRoute;
        this.invocationHook = invocationHook;
        this.suggestionHook = suggestionHook;
        this.routeUUID = baseRoute.getUniqueId();
    }

    public UUID routeUUID() {
        return routeUUID;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> baseArgument = LiteralArgumentBuilder.literal(baseRoute.getName());

        appendRoute(baseRoute, baseArgument);
        dispatcher.register(baseArgument);
    }

    private void appendRoute(CommandRoute<ServerCommandSource> route, ArgumentBuilder<ServerCommandSource, ?> baseArgument) {
        for (CommandExecutor<ServerCommandSource> executor : route.getExecutors()) {
            appendExecutor(baseArgument, route, executor);
        }
    }

    private void appendExecutor(ArgumentBuilder<ServerCommandSource, ?> baseArgument, CommandRoute<ServerCommandSource> route, CommandExecutor<ServerCommandSource> executor) {
        ArgumentBuilder<ServerCommandSource, ?> literal = route == baseRoute ? baseArgument : LiteralArgumentBuilder.literal(route.getName());
        RequiredArgumentBuilder<ServerCommandSource, String> arguments = RequiredArgumentBuilder.argument("[...]", StringArgumentType.greedyString());
        arguments.executes(context -> {
            String input = context.getInput();
            RawCommand rawCommand = RawCommand.from(input);
            ParseableInput<?> parseableInput = rawCommand.toParseableInput();
            FabricSender platformSender = new FabricSender(context.getSource());
            Invocation<ServerCommandSource> invocation = new Invocation<>(context.getSource(), platformSender, this.baseRoute.getName(), rawCommand.getLabel(), parseableInput);

            invocationHook.execute(invocation, parseableInput);
            return 1;
        });
        arguments.suggests(new SuggestionProviderImpl(executor));

        literal.then(arguments);
        for (CommandRoute<ServerCommandSource> child : route.getChildren()) {
            appendRoute(child, literal);
        }
        if (literal != baseArgument) {
            baseArgument.then(literal);
        }
    }

    public class SuggestionProviderImpl implements SuggestionProvider<ServerCommandSource> {
        private final CommandExecutor<ServerCommandSource> executor;

        public SuggestionProviderImpl(CommandExecutor<ServerCommandSource> executor) {
            this.executor = executor;
        }

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            String input = context.getInput();
            RawCommand rawCommand = RawCommand.from(input);
            SuggestionInput<?> suggestionInput = rawCommand.toSuggestionInput();
            FabricSender platformSender = new FabricSender(context.getSource());
            Invocation<ServerCommandSource> invocation = new Invocation<>(context.getSource(), platformSender, baseRoute.getName(), rawCommand.getLabel(), suggestionInput);

            SuggestionResult suggest = suggestionHook.suggest(invocation, suggestionInput);
            String[] args = rawCommand.getArgs().toArray(new String[0]);
            for (String s : suggest.asMultiLevelList()) {
                if (s.isBlank()) {
                    continue;
                }
                int start = input.length() - args[args.length - 1].length();
                SuggestionsBuilder suggestionsBuilder = new SuggestionsBuilder(builder.getInput(), builder.getInput().toLowerCase(), start);
                suggestionsBuilder.suggest(s);
                builder.add(suggestionsBuilder);
            }
            return builder.buildFuture();
        }
    }
}
