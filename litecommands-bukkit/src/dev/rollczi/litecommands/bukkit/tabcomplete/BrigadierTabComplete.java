package dev.rollczi.litecommands.bukkit.tabcomplete;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.bukkit.BukkitCommand;
import dev.rollczi.litecommands.bukkit.tabcomplete.brigadier.BrigadierTranslator;
import dev.rollczi.litecommands.bukkit.tabcomplete.brigadier.Brigadier;
import dev.rollczi.litecommands.bukkit.tabcomplete.brigadier.PaperBrigadier;
import dev.rollczi.litecommands.command.CommandRoute;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

class BrigadierTabComplete extends TabCompletePaperAsync {

    private final Brigadier brigadier;
    private final BrigadierTranslator<CommandSender> brigadierTranslator;

    public BrigadierTabComplete(Plugin plugin, SuggesterRegistry<CommandSender> suggesterRegistry, ParserRegistry<CommandSender> parserRegistry) {
        super(plugin);
        this.brigadier = new PaperBrigadier(plugin);
        this.brigadierTranslator = new BrigadierTranslator<>(suggesterRegistry, parserRegistry);
    }

    @Override
    public void register(String fallbackPrefix, BukkitCommand listener, CommandRoute<?> commandRoute) {
        super.register(fallbackPrefix, listener, commandRoute);

        for (LiteralArgumentBuilder<CommandSender> builder : brigadierTranslator.translate((CommandRoute<CommandSender>) commandRoute)) {
            brigadier.register(builder);
        }
    }

}
