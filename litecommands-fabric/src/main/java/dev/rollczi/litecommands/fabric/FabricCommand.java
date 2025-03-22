package dev.rollczi.litecommands.fabric;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.PermissionService;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.platform.PlatformSenderFactory;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FabricCommand<SOURCE> {

    private final PlatformSenderFactory<SOURCE> senderFactory;
    private final LiteFabricSettings settings;
    private final CommandRoute<SOURCE> baseRoute;
    private final PlatformInvocationListener<SOURCE> invocationHook;
    private final PlatformSuggestionListener<SOURCE> suggestionHook;
    private final PermissionService permissionService;

    public FabricCommand(PlatformSenderFactory<SOURCE> senderFactory, LiteFabricSettings settings, CommandRoute<SOURCE> baseRoute, PlatformInvocationListener<SOURCE> invocationHook, PlatformSuggestionListener<SOURCE> suggestionHook, PermissionService permissionService) {
        this.senderFactory = senderFactory;
        this.settings = settings;
        this.baseRoute = baseRoute;
        this.invocationHook = invocationHook;
        this.suggestionHook = suggestionHook;
        this.permissionService = permissionService;
    }

    public LiteralArgumentBuilder<SOURCE> toLiteral() {
        LiteralArgumentBuilder<SOURCE> baseArgument = LiteralArgumentBuilder.<SOURCE>literal(baseRoute.getName())
            .requires(context -> !settings.isNativePermission() || permissionService.isPermitted(senderFactory.create(context), baseRoute))
            .executes(context -> execute(context));
        this.appendRoute(baseArgument, baseRoute);
        return baseArgument;
    }

    private void appendRoute(LiteralArgumentBuilder<SOURCE> baseLiteral, CommandRoute<SOURCE> route) {
        boolean isBase = route == baseRoute;
        LiteralArgumentBuilder<SOURCE> literal = isBase
            ? baseLiteral
            : LiteralArgumentBuilder.<SOURCE>literal(route.getName())
                .requires(source -> this.hasAnyPermission(source, route))
                .executes(context -> execute(context));

        literal.then(this.createArguments(route));

        for (CommandRoute<SOURCE> child : route.getChildren()) {
            this.appendRoute(literal, child);
        }
        if (!isBase) {
            baseLiteral.then(literal);
        }
    }

    @NotNull
    private RequiredArgumentBuilder<SOURCE, String> createArguments(CommandRoute<SOURCE> route) {
        return RequiredArgumentBuilder
            .<SOURCE, String>argument(settings.getInputInspectionDisplay(), StringArgumentType.greedyString())
            .requires(source -> hasAnyPermission(source, route))
            .executes(context -> execute(context))
            .suggests((context, builder) -> suggests(context, builder));
    }

    private boolean hasAnyPermission(SOURCE source, CommandRoute<SOURCE> route) {
        PlatformSender sender = senderFactory.create(source);
        for (CommandExecutor<SOURCE> child : route.getExecutors()) {
            if (permissionService.isPermitted(sender, child)) {
                return true;
            }
        }

        return false;
    }

    private int execute(CommandContext<SOURCE> context) {
        RawCommand rawCommand = RawCommand.from(context.getInput());
        ParseableInput<?> parseableInput = rawCommand.toParseableInput();
        PlatformSender platformSender = this.senderFactory.create(context.getSource());
        Invocation<SOURCE> invocation = new Invocation<>(platformSender, baseRoute.getName(), rawCommand.getLabel(), parseableInput);

        invocationHook.execute(invocation, parseableInput);
        return Command.SINGLE_SUCCESS;
    }

    private @NotNull CompletableFuture<Suggestions> suggests(CommandContext<SOURCE> context, SuggestionsBuilder builder) {
        return CompletableFuture.supplyAsync(() -> {
            String input = context.getInput();
            RawCommand rawCommand = RawCommand.from(input);
            SuggestionInput<?> suggestionInput = rawCommand.toSuggestionInput();
            PlatformSender platformSender = this.senderFactory.create(context.getSource());
            Invocation<SOURCE> invocation = new Invocation<>(platformSender, baseRoute.getName(), rawCommand.getLabel(), suggestionInput);

            SuggestionResult suggest = suggestionHook.suggest(invocation, suggestionInput);

            List<String> arguments = suggestionInput.asList();
            int start = input.length() - arguments.get(arguments.size() - 1).length();
            SuggestionsBuilder suggestionsBuilder = builder.createOffset(start);

            for (Suggestion suggestion : suggest.getSuggestions()) {
                suggestionsBuilder.suggest(suggestion.multilevel(), tooltip(suggestion.tooltip()));// todo Text.Serialization#fromJson?
            }

            return suggestionsBuilder.build();
        });
    }

    Text tooltip(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return Text.literal(string);
    }

    public CommandRoute<SOURCE> getCommandRoute() {
        return baseRoute;
    }
}
