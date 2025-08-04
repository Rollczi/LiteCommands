package dev.rollczi.litecommands.bukkit.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.Locale;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class NamespacedKeyArgument extends ArgumentResolver<CommandSender, NamespacedKey> {

    private final Plugin plugin;
    private final MessageRegistry<CommandSender> messageRegistry;

    public NamespacedKeyArgument(Plugin plugin, MessageRegistry<CommandSender> messageRegistry) {
        this.plugin = plugin;
        this.messageRegistry = messageRegistry;
    }

    @Override
    protected ParseResult<NamespacedKey> parse(Invocation<CommandSender> invocation, Argument<NamespacedKey> context, String argument) {
        NamespacedKey parsed = NamespacedKey.fromString(argument);
        if (parsed == null) {
            return ParseResult.failure(messageRegistry.get(LiteBukkitMessages.NAMESPACED_KEY_INVALID, invocation, argument));
        }

        return ParseResult.success(parsed);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<NamespacedKey> argument, SuggestionContext context) {
        String pluginName = plugin.getName().toLowerCase(Locale.ROOT).replace(" ", "-");
        return SuggestionResult.of("namespace:key", pluginName + ":key");
    }

}
