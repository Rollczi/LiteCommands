package dev.rollczi.litecommands.folia.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.folia.LiteFoliaMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleOnlyContextProvider implements ContextProvider<CommandSender, ConsoleCommandSender> {

    private final MessageRegistry<CommandSender> messageRegistry;

    public ConsoleOnlyContextProvider(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<ConsoleCommandSender> provide(Invocation<CommandSender> invocation) {
        if (invocation.sender() instanceof ConsoleCommandSender) {
            return ContextResult.ok(() -> (ConsoleCommandSender) invocation.sender());
        }

        return ContextResult.error(messageRegistry.getInvoked(LiteFoliaMessages.CONSOLE_ONLY, invocation));
    }

}
