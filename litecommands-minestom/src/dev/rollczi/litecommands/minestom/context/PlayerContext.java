package dev.rollczi.litecommands.minestom.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.minestom.LiteMinestomMessages;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public class PlayerContext implements ContextProvider<CommandSender, Player> {

    private final MessageRegistry<CommandSender> messageRegistry;

    public PlayerContext(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<Player> provide(Invocation<CommandSender> invocation) {
        CommandSender sender = invocation.sender();

        if (sender instanceof Player player) {
            return ContextResult.ok(() -> player);
        }

        return ContextResult.error(messageRegistry.getInvoked(LiteMinestomMessages.PLAYER_ONLY, invocation));
    }

}
