package dev.rollczi.litecommands.bukkit.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

public class NamespacedKeyArgument extends ArgumentResolver<CommandSender, NamespacedKey> {
    private final MessageRegistry<CommandSender> messageRegistry;
    public NamespacedKeyArgument(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    protected ParseResult<NamespacedKey> parse(Invocation<CommandSender> invocation, Argument<NamespacedKey> context, String argument) {
        final NamespacedKey parsed = NamespacedKey.fromString(argument);
        if (parsed == null) {
            return ParseResult.failure(messageRegistry.get(LiteBukkitMessages.NAMESPACED_KEY_INVALID, invocation, argument));
        } else {
            return ParseResult.success(parsed);
        }
    }
}
