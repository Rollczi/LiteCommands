package dev.rollczi.litecommands.folia.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.folia.LiteFoliaMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerOnlyContextProvider implements ContextProvider<CommandSender, Player> {

    private final MessageRegistry<CommandSender> messageRegistry;

    public PlayerOnlyContextProvider(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<Player> provide(Invocation<CommandSender> invocation) {
        if (invocation.sender() instanceof Player) {
            return ContextResult.ok(() -> (Player) invocation.sender());
        }

        return ContextResult.error(messageRegistry.getInvoked(LiteFoliaMessages.PLAYER_ONLY, invocation));
    }

}
