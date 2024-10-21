package dev.rollczi.litecommands.fabric.server.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.fabric.LiteFabricMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WorldArgument<W> extends ArgumentResolver<ServerCommandSource, W> {

    private final MessageRegistry<ServerCommandSource> messageRegistry;

    public WorldArgument(MessageRegistry<ServerCommandSource> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ParseResult<W> parse(Invocation<ServerCommandSource> invocation, Argument<W> context, String argument) {
        MinecraftServer server = invocation.sender().getServer();
        RegistryKey<World> worldRegistryKey = null;
        for (RegistryKey<World> key : server.getWorldRegistryKeys()) {
            if (key.getValue().toString().equalsIgnoreCase(argument) || key.getValue().getPath().equalsIgnoreCase(argument)) {
                worldRegistryKey = key;
                break;
            }
        }
        if (worldRegistryKey == null) {
            return ParseResult.failure(messageRegistry.getInvoked(LiteFabricMessages.WORLD_NOT_EXIST, invocation, argument));
        }
        World world = server.getWorld(worldRegistryKey);

        if (world == null) {
            return ParseResult.failure(messageRegistry.getInvoked(LiteFabricMessages.WORLD_NOT_EXIST, invocation, argument));
        }

        return ParseResult.success((W) world);
    }

    @Override
    public SuggestionResult suggest(Invocation<ServerCommandSource> invocation, Argument<W> argument, SuggestionContext context) {
        MinecraftServer server = invocation.sender().getServer();
        List<ServerWorld> worldList = new ArrayList<>();
        for (ServerWorld world : server.getWorlds()) {
            worldList.add(world);
        }
        return worldList.stream()
            .map(this::getWorldName)
            .collect(SuggestionResult.collector());
    }

    private String getWorldName(ServerWorld world) {
        return world.getRegistryKey().getValue().toString();
    }

}
