package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class LiteBukkitAdventureFactory {

    private LiteBukkitAdventureFactory() {
    }

    @ApiStatus.Internal
    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, boolean nativePermissions, KyoriAudienceProvider kyoriAudienceProvider, boolean supportsMiniMessage) {
        ComponentSerializer<Component, ? extends Component, String> serializer = supportsMiniMessage
            ? MiniMessageFactory.produce()
            : LegacyProcessor.LEGACY_SERIALIZER;

        return builder(server, fallbackPrefix, nativePermissions, kyoriAudienceProvider, serializer);
    }

    @ApiStatus.Internal
    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, boolean nativePermissions, KyoriAudienceProvider kyoriAudienceProvider, ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        return LiteBukkitFactory.builder(server, fallbackPrefix, nativePermissions)
            .argument(Joiner.class, Component.class, new KyoriComponentJoinerArgument(kyoriComponentSerializer))
            .argument(Component.class, new KyoriComponentArgument())
            .argument(Component.class, "color", new KyoriColoredComponentArgument(kyoriComponentSerializer))

            .contextualBind(Audience.class, new KyoriAudienceContextual(kyoriAudienceProvider))

            .resultHandler(Component.class, new KyoriComponentHandler(kyoriAudienceProvider))
            .resultHandler(String.class, new StringHandler(kyoriAudienceProvider, kyoriComponentSerializer));
    }

}
