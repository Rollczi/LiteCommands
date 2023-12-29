package dev.rollczi.litecommands.bukkit.tabcomplete.brigadier;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class PaperBrigadier extends AbstractBrigadier implements Brigadier, Listener {

    static {
        try {
            Class.forName("com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent");
        } catch (ClassNotFoundException exception) {
            throw new UnsupportedOperationException("No paper!", exception);
        }
    }

    private final List<BrigadierCommandNode> commands = new ArrayList<>();

    public PaperBrigadier(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void register(LiteralCommandNode<?> node) {
        Objects.requireNonNull(node, "node");

        this.commands.add(new BrigadierCommandNode(node, null));
    }

    @EventHandler
    @SuppressWarnings("UnstableApiUsage")
    public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
        if (event.isAsynchronous() || !event.hasFiredAsync()) {
            for (BrigadierCommandNode command : this.commands) {
                command.apply(event.getPlayer(), event.getCommandNode());
            }
        }
    }

    private static final class BrigadierCommandNode {
        private final LiteralCommandNode<?> node;
        private final Predicate<? super Player> permissionTest;

        private BrigadierCommandNode(LiteralCommandNode<?> node, Predicate<? super Player> permissionTest) {
            this.node = node;
            this.permissionTest = permissionTest;
        }

        public void apply(Player player, RootCommandNode<?> root) {
            if (this.permissionTest != null && !this.permissionTest.test(player)) {
                return;
            }
            removeChild(root, this.node.getName());
            root.addChild((CommandNode) this.node);
        }
    }

}