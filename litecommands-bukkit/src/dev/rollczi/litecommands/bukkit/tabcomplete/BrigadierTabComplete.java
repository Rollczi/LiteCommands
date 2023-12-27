package dev.rollczi.litecommands.bukkit.tabcomplete;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.rollczi.litecommands.bukkit.BukkitCommand;
import dev.rollczi.litecommands.command.CommandRoute;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.plugin.Plugin;

// TODO: Extends Paper
public class BrigadierTabComplete extends AbstractAsyncTabComplete {

    private final Commodore commodore;

    public BrigadierTabComplete(Plugin plugin) {
        this.commodore = CommodoreProvider.getCommodore(plugin);

        if (commodore == null) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void register(String fallbackPrefix, BukkitCommand listener, CommandRoute<?> commandRoute) {
        super.register(fallbackPrefix, listener, commandRoute);
        for (LiteralArgumentBuilder<?> builder : new BrigadierTranslator().translate(commandRoute)) {
            commodore.register(builder);
        }
    }

}
