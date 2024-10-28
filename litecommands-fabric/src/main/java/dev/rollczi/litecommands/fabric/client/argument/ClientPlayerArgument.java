package dev.rollczi.litecommands.fabric.client.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.fabric.LiteFabricMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class ClientPlayerArgument<P extends PlayerEntity> extends ArgumentResolver<FabricClientCommandSource, P> {
    private final MessageRegistry<FabricClientCommandSource> messageRegistry;

    public ClientPlayerArgument(MessageRegistry<FabricClientCommandSource> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ParseResult<P> parse(Invocation<FabricClientCommandSource> invocation, Argument<P> context, String argument) {
        AbstractClientPlayerEntity player = invocation.sender().getWorld().getPlayers().stream().filter(p -> p.getGameProfile().getName().equalsIgnoreCase(argument)).findFirst().orElse(null);

        if (player != null) {
            return ParseResult.success((P) player);
        }

        return ParseResult.failure(messageRegistry.getInvoked(LiteFabricMessages.PLAYER_NOT_FOUND, invocation, argument));
    }

    @Override
    public SuggestionResult suggest(Invocation<FabricClientCommandSource> invocation, Argument<P> argument, SuggestionContext context) {
        return SuggestionResult.from(
            invocation.sender().getWorld().getPlayers().stream()
                .map(player -> player.getGameProfile())
                .map(gameProfile -> Suggestion.of(gameProfile.getName(), gameProfile.getId().toString()))
                .collect(Collectors.toList())
        );
    }
}
