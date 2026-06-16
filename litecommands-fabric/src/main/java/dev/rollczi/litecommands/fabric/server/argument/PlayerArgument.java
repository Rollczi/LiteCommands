package dev.rollczi.litecommands.fabric.server.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.fabric.LiteFabricMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.players.PlayerList;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class PlayerArgument<P extends Player> extends ArgumentResolver<CommandSourceStack, P> {

    private final MessageRegistry<CommandSourceStack> messageRegistry;

    public PlayerArgument(MessageRegistry<CommandSourceStack> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ParseResult<P> parse(Invocation<CommandSourceStack> invocation, Argument<P> context, String argument) {
        PlayerList playerList = invocation.sender().getServer().getPlayerList();
        ServerPlayer player = playerList.getPlayerByName(argument);

        if (player != null) {
            return ParseResult.success((P) player);
        }

        return ParseResult.failure(messageRegistry.get(LiteFabricMessages.PLAYER_NOT_FOUND, invocation, argument));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSourceStack> invocation, Argument<P> argument, SuggestionContext context) {
        return invocation.sender().getServer().getPlayerList().getPlayers().stream()
            .map(serverPlayer -> serverPlayer.getGameProfile())
            .collect(SuggestionResult.collector(profile -> profile.name(), profile -> profile.id().toString()));
    }

}
