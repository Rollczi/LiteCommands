package dev.rollczi.litecommands.bukkit.tabcomplete.brigadier;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

public interface Brigadier {

    void register(LiteralCommandNode<?> node);

    default void register(LiteralArgumentBuilder<?> argumentBuilder) {
        register(argumentBuilder.build());
    }

}