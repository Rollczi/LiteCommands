package dev.rollczi.litecommands.minestom.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.minestom.LiteMinestomMessages;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minestom.server.command.CommandSender;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;

public class InstanceArgument extends ArgumentResolver<CommandSender, Instance> {

    private final InstanceManager instanceManager;
    private final MessageRegistry<CommandSender> messageRegistry;

    public InstanceArgument(InstanceManager instanceManager, MessageRegistry<CommandSender> messageRegistry) {
        this.instanceManager = instanceManager;
        this.messageRegistry = messageRegistry;
    }

    @Override
    protected ParseResult<Instance> parse(Invocation<CommandSender> invocation, Argument<Instance> context, String argument) {
        Instance instance = this.instanceManager.getInstances().stream()
            .filter(current -> current.getDimensionName().equals(argument))
            .findFirst()
            .orElse(null);

        if (instance == null) {
            return ParseResult.failure(messageRegistry.get(LiteMinestomMessages.INSTANCE_NOT_FOUND, invocation, argument));
        }

        return ParseResult.success(instance);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Instance> argument, SuggestionContext context) {
        return this.instanceManager.getInstances().stream()
            .map(instance -> instance.getDimensionName())
            .collect(SuggestionResult.collector());
    }

}