package dev.rollczi.litecommands.bukkit.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;

public class WorldArgument extends ArgumentResolver<CommandSender, World> {

    private final Server server;
    private final MessageRegistry<CommandSender> messageRegistry;

    public WorldArgument(Server server, MessageRegistry<CommandSender> messageRegistry) {
        this.server = server;
        this.messageRegistry = messageRegistry;
    }

    @Override
    protected ParseResult<World> parse(Invocation<CommandSender> invocation, Argument<World> context, String argument) {
        World world = server.getWorld(argument);

        if (world == null) {
            return ParseResult.failure(messageRegistry.getInvoked(LiteBukkitMessages.WORLD_NOT_EXIST, invocation, argument));
        }

        return ParseResult.success(world);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<World> argument, SuggestionContext context) {
        return this.server.getWorlds().stream()
            .map(world -> this.getWorldName(world))
            .collect(SuggestionResult.collector());
    }

    // in some minecraft versions, the world name is not in the world info class
    private String getWorldName(World world) {
        Class<?> worldClass = world.getClass();
        try {
            return (String) worldClass.getMethod("getName").invoke(world);
        }
        catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException exception) {
            throw new LiteCommandsException(exception);
        }
    }

}
