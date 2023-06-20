package dev.rollczi.example.bukkit.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.argument.suggestion.SuggestionContext;
import dev.rollczi.litecommands.argument.suggestion.SuggestionResult;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.WorldInfo;

public class WorldArgument extends ArgumentResolver<CommandSender, World> {

    private final Server server;

    public WorldArgument(Server server) {
        this.server = server;
    }

    @Override
    protected ParseResult<World> parse(Invocation<CommandSender> invocation, Argument<World> context, String argument) {
        World world = server.getWorld(argument);

        if (world == null) {
            return ParseResult.failure("World '" + argument + "' not exists");
        }

        return ParseResult.success(() -> world);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<World> argument, SuggestionContext context) {
        return this.server.getWorlds().stream()
            .map(WorldInfo::getName)
            .collect(SuggestionResult.collector());
    }

}
