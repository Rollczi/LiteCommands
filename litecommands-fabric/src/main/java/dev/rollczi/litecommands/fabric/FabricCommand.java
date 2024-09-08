package dev.rollczi.litecommands.fabric;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

class FabricCommand {

    private static final String FULL_ARGUMENTS = "[...]";

    private final CommandRoute<ServerCommandSource> baseRoute;
    private final PlatformInvocationListener<ServerCommandSource> invocationHook;
    private final PlatformSuggestionListener<ServerCommandSource> suggestionHook;

    FabricCommand(CommandRoute<ServerCommandSource> baseRoute, PlatformInvocationListener<ServerCommandSource> invocationHook, PlatformSuggestionListener<ServerCommandSource> suggestionHook) {
        this.baseRoute = baseRoute;
        this.invocationHook = invocationHook;
        this.suggestionHook = suggestionHook;
    }

    LiteralArgumentBuilder<ServerCommandSource> toLiteral() {
        LiteralArgumentBuilder<ServerCommandSource> baseArgument = LiteralArgumentBuilder.literal(baseRoute.getName());

        this.appendRoute(baseArgument, baseRoute);
        return baseArgument;
    }

    private void appendRoute(LiteralArgumentBuilder<ServerCommandSource> baseLiteral, CommandRoute<ServerCommandSource> route) {
        boolean isBase = route == baseRoute;
        LiteralArgumentBuilder<ServerCommandSource> literal = isBase
            ? baseLiteral
            : LiteralArgumentBuilder.literal(route.getName());

        literal.then(this.createArguments());

        for (CommandRoute<ServerCommandSource> child : route.getChildren()) {
            this.appendRoute(literal, child);
        }
        if (!isBase) {
            baseLiteral.then(literal);
        }
    }

    @NotNull
    private RequiredArgumentBuilder<ServerCommandSource, String> createArguments() {
        return RequiredArgumentBuilder
            .<ServerCommandSource, String>argument(FULL_ARGUMENTS, StringArgumentType.greedyString())
            .executes(context -> {
                RawCommand rawCommand = RawCommand.from(context.getInput());
                ParseableInput<?> parseableInput = rawCommand.toParseableInput();
                FabricSender platformSender = new FabricSender(context.getSource());
                Invocation<ServerCommandSource> invocation = new Invocation<>(context.getSource(), platformSender, baseRoute.getName(), rawCommand.getLabel(), parseableInput);

                invocationHook.execute(invocation, parseableInput);
                return Command.SINGLE_SUCCESS;
            })
            .suggests((context, builder) -> CompletableFuture.supplyAsync(() -> {
                String input = context.getInput();
                RawCommand rawCommand = RawCommand.from(input);
                SuggestionInput<?> suggestionInput = rawCommand.toSuggestionInput();
                FabricSender platformSender = new FabricSender(context.getSource());
                Invocation<ServerCommandSource> invocation = new Invocation<>(context.getSource(), platformSender, baseRoute.getName(), rawCommand.getLabel(), suggestionInput);

                SuggestionResult suggest = suggestionHook.suggest(invocation, suggestionInput);

                List<String> arguments = suggestionInput.asList();
                int start = input.length() - arguments.get(arguments.size() - 1).length();
                SuggestionsBuilder suggestionsBuilder = builder.createOffset(start);

                for (String suggestion : suggest.asMultiLevelList()) {
                    suggestionsBuilder.suggest(suggestion);
                }

                return suggestionsBuilder.build();
            }));
    }

}
