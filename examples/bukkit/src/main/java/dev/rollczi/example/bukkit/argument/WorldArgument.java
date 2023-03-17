package dev.rollczi.example.bukkit.argument;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.type.OneArgumentResolver;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.suggestion.SuggestionContext;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResult;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.WorldInfo;

public class WorldArgument extends OneArgumentResolver<CommandSender, World> {

    private final Server server;

    public WorldArgument(Server server) {
        this.server = server;
    }

    @Override
    protected ArgumentResult<World> parse(Invocation<CommandSender> invocation, Argument<World> context, String argument) {
        World world = server.getWorld(argument);

        if (world == null) {
            return ArgumentResult.failure("World '" + argument + "' not exists");
        }

        return ArgumentResult.success(() -> world);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<World> argument, SuggestionContext suggestion) {
        return this.server.getWorlds().stream()
            .map(WorldInfo::getName)
            .collect(SuggestionResult.COLLECTOR);
    }

}
