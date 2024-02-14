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
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
        List<RequiredArgumentBuilder<ServerCommandSource, String>> arguments = arguments(executor.getArguments());
        if (arguments != null) {
            for (RequiredArgumentBuilder<ServerCommandSource, String> argument : arguments) {
                argument.suggests(new SuggestionProviderImpl(executor));
            }
            arguments.get(arguments.size() - 1).executes(context -> {
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
            Iterator<RequiredArgumentBuilder<ServerCommandSource, String>> iterator = arguments.iterator();
            RequiredArgumentBuilder<ServerCommandSource, String> first = iterator.next();
            RequiredArgumentBuilder<ServerCommandSource, String> last = first;
            while (iterator.hasNext()) {
                RequiredArgumentBuilder<ServerCommandSource, String> next = iterator.next();
                last.then(next);
                last = next;
            }
            literal.then(first);
        }
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

            for (String s : suggestionHook.suggest(invocation, suggestionInput).asMultiLevelList()) {
                builder.suggest(s);
            }
            return builder.buildFuture();
        }
    }

    private List<RequiredArgumentBuilder<ServerCommandSource, String>> arguments(List<Argument<?>> arguments) {
        Iterator<Argument<?>> iterator = arguments.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        List<RequiredArgumentBuilder<ServerCommandSource, String>> list = new ArrayList<>();
        Argument<?> root = iterator.next();

        RequiredArgumentBuilder<ServerCommandSource, String> first = RequiredArgumentBuilder.argument(root.getName(), argumentType(root));
        list.add(first);
        while (iterator.hasNext()) {
            Argument<?> argument1 = iterator.next();
            RequiredArgumentBuilder<ServerCommandSource, String> argument = RequiredArgumentBuilder.argument(argument1.getName(), argumentType(argument1));
            list.add(argument);
        }
        return list;
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

    private static LiteralArgumentBuilder<ServerCommandSource> namesLiteral(Collection<String> names) {
        Iterator<String> iterator = names.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        String root = iterator.next();
        LiteralArgumentBuilder<ServerCommandSource> literal = LiteralArgumentBuilder.literal(root);
        while (iterator.hasNext()) {
            literal.then(LiteralArgumentBuilder.literal(iterator.next()));
        }
        return literal;
    }
}
