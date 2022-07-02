package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LiteBukkitAdventureFactory {

    private LiteBukkitAdventureFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, AudienceProvider audienceProvider) {
        KyoriComponentSender kyoriComponentSender = new KyoriComponentSender(audienceProvider);

        return LiteBukkitFactory.builder(server, fallbackPrefix)
                .resultHandler(Component.class, new KyoriComponentHandler(kyoriComponentSender))
                .resultHandler(String.class, new StringHandler(kyoriComponentSender));
    }

}
