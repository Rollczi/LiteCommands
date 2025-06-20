package dev.rollczi.litecommands.minestom.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.minestom.LiteMinestomMessages;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;

public class ConsoleOnlyContextProvider implements ContextProvider<CommandSender, ConsoleSender> {

    private final MessageRegistry<CommandSender> messageRegistry;

    public ConsoleOnlyContextProvider(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<ConsoleSender> provide(Invocation<CommandSender> invocation) {
        CommandSender sender = invocation.sender();

        if (sender instanceof ConsoleSender console) {
            return ContextResult.ok(() -> console);
        }

        return ContextResult.error(messageRegistry.get(LiteMinestomMessages.CONSOLE_ONLY, invocation));
    }

}
