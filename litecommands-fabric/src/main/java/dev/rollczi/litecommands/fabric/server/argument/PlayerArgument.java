package dev.rollczi.litecommands.fabric.server.argument;

import com.mojang.authlib.GameProfile;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.fabric.LiteFabricMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerArgument<P extends PlayerEntity> extends ArgumentResolver<ServerCommandSource, P> {

    private final MessageRegistry<ServerCommandSource> messageRegistry;

    public PlayerArgument(MessageRegistry<ServerCommandSource> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ParseResult<P> parse(Invocation<ServerCommandSource> invocation, Argument<P> context, String argument) {
        PlayerManager playerManager = invocation.sender().getServer().getPlayerManager();
        ServerPlayerEntity player = playerManager.getPlayer(argument);

        if (player != null) {
            return ParseResult.success((P) player);
        }

        return ParseResult.failure(messageRegistry.getInvoked(LiteFabricMessages.PLAYER_NOT_FOUND, invocation, argument));
    }

    @Override
    public SuggestionResult suggest(Invocation<ServerCommandSource> invocation, Argument<P> argument, SuggestionContext context) {
        return invocation.sender().getServer().getPlayerManager().getPlayerList().stream()
            .map(ServerPlayerEntity::getGameProfile)
            .map(GameProfile::getName)
            .collect(SuggestionResult.collector());
    }

}
