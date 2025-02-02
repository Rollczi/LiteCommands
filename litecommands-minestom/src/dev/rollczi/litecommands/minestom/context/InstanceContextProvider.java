package dev.rollczi.litecommands.minestom.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.minestom.LiteMinestomMessages;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public class InstanceContextProvider implements ContextProvider<CommandSender, Instance> {

    private final MessageRegistry<CommandSender> messageRegistry;

    public InstanceContextProvider(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<Instance> provide(Invocation<CommandSender> invocation) {
        if (invocation.sender() instanceof Player player) {
            Instance instance = player.getInstance();

            if (instance == null) {
                return ContextResult.error(messageRegistry.getInvoked(LiteMinestomMessages.PLAYER_IS_NOT_IN_INSTANCE, invocation, player));
            }

            return ContextResult.ok(() -> instance);
        }

        return ContextResult.error(messageRegistry.getInvoked(LiteMinestomMessages.PLAYER_ONLY, invocation));
    }

}