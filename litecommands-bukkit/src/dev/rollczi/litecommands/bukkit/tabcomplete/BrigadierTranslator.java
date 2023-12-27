package dev.rollczi.litecommands.bukkit.tabcomplete;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrigadierTranslator {

    public <T> List<LiteralArgumentBuilder<T>> translate(CommandRoute<?> route) {
        List<LiteralArgumentBuilder<T>> argumentBuilders = new ArrayList<>();

        for (String name : route.names()) {
            LiteralArgumentBuilder<T> builder = LiteralArgumentBuilder.literal(name);

            for (CommandRoute<?> child : route.getChildren()) {
                List<LiteralArgumentBuilder<T>> translated = translate(child);

                for (LiteralArgumentBuilder<T> literalArgumentBuilder : translated) {
                    builder.then(literalArgumentBuilder);
                }
            }

            for (CommandExecutor<?> executor : route.getExecutors()) {
                for (ArgumentBuilder<T, ?> argumentBuilder : this.<T>translate(executor)) {
                    builder.then(argumentBuilder);
                }
            }

            argumentBuilders.add(builder);
        }

        return argumentBuilders;
    }

    private <T> List<ArgumentBuilder<T, ?>> translate(CommandExecutor<?> executor) {
        ArgumentBuilder<T, ?> argumentBuilder = null;

        for (Argument<?> argument : executor.getArguments()) {
            // TODO better handling of arguments
            ArgumentBuilder<T, ?> builder = RequiredArgumentBuilder.argument(argument.getName(), StringArgumentType.word());

            if (argumentBuilder == null) {
                argumentBuilder = builder;
                continue;
            }

            argumentBuilder.then(builder);
        }

        if (argumentBuilder == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(argumentBuilder);
    }


}
