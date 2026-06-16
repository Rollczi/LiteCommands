package dev.rollczi.litecommands.fabric.server.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.fabric.LiteFabricMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class WorldArgument<W> extends ArgumentResolver<CommandSourceStack, W> {

    private final MessageRegistry<CommandSourceStack> messageRegistry;

    public WorldArgument(MessageRegistry<CommandSourceStack> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ParseResult<W> parse(Invocation<CommandSourceStack> invocation, Argument<W> context, String argument) {
        MinecraftServer server = invocation.sender().getServer();
        ResourceKey<Level> worldRegistryKey = null;
        for (ResourceKey<Level> key : server.levelKeys()) {
            if (key.identifier().toString().equalsIgnoreCase(argument) || key.identifier().getPath().equalsIgnoreCase(argument)) {
                worldRegistryKey = key;
                break;
            }
        }
        if (worldRegistryKey == null) {
            return ParseResult.failure(messageRegistry.get(LiteFabricMessages.WORLD_NOT_EXIST, invocation, argument));
        }
        Level world = server.getLevel(worldRegistryKey);

        if (world == null) {
            return ParseResult.failure(messageRegistry.get(LiteFabricMessages.WORLD_NOT_EXIST, invocation, argument));
        }

        return ParseResult.success((W) world);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSourceStack> invocation, Argument<W> argument, SuggestionContext context) {
        MinecraftServer server = invocation.sender().getServer();
        List<ServerLevel> worldList = new ArrayList<>();
        for (ServerLevel world : server.getAllLevels()) {
            worldList.add(world);
        }
        return worldList.stream()
            .map(world -> getWorldName(world))
            .collect(SuggestionResult.collector());
    }

    private String getWorldName(ServerLevel world) {
        return world.dimension().identifier().toString();
    }

}
