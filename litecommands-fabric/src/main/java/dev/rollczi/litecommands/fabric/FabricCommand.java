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
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.join.JoinArgument;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.quoted.QuotedStringArgumentResolver;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

/**
 * 2024/2/13<br>
 * LiteCommands<br>
 *
 * @author huanmeng_qwq
 */
public class FabricCommand {
    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);

    static List<FabricCommand> INSTANCES = new ArrayList<>();
    private final CommandRoute<ServerCommandSource> baseRoute;
    private final PlatformInvocationListener<ServerCommandSource> invocationHook;
    private final PlatformSuggestionListener<ServerCommandSource> suggestionHook;
    private final UUID routeUUID;

    public FabricCommand(CommandRoute<ServerCommandSource> baseRoute, PlatformInvocationListener<ServerCommandSource> invocationHook, PlatformSuggestionListener<ServerCommandSource> suggestionHook) {
        this.baseRoute = baseRoute;
        this.invocationHook = invocationHook;
        this.suggestionHook = suggestionHook;
        this.routeUUID = baseRoute.getUniqueId();
        INSTANCES.add(this);
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
            String rootName = input;
            String[] args = {};
            int index = input.indexOf(" ");
            if (index != -1) {
                rootName = input.substring(0, index);
                String argLine = input.substring(index + 1);
                args = argLine.split(" ");
            }
            ParseableInput<?> parseableInput = ParseableInput.raw(args);
            FabricSender platformSender = new FabricSender(context.getSource());
            Invocation<ServerCommandSource> invocation = new Invocation<>(context.getSource(), platformSender, this.baseRoute.getName(), rootName, parseableInput);

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
            String rootName = input;
            String[] args = {""};
            int index = input.indexOf(" ");
            if (index != -1) {
                rootName = input.substring(0, index);
                String argLine = input.substring(index + 1);
                args = PATTERN_ON_SPACE.split(argLine, -1);
            }
            SuggestionInput<?> suggestionInput = SuggestionInput.raw(args);
            FabricSender platformSender = new FabricSender(context.getSource());
            Invocation<ServerCommandSource> invocation = new Invocation<>(context.getSource(), platformSender, baseRoute.getName(), rootName, suggestionInput);

            SuggestionResult suggest = suggestionHook.suggest(invocation, suggestionInput);
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

    private static StringArgumentType argumentType(Argument<?> argument) {
        if (QuotedStringArgumentResolver.KEY.equals(argument.getKeyName())) {
            return StringArgumentType.string();
        }
        if (argument instanceof JoinArgument) {
            return StringArgumentType.greedyString();
        }
        return StringArgumentType.word();
    }
}
