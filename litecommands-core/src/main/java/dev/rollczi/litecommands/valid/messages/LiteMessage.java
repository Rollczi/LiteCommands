package dev.rollczi.litecommands.valid.messages;

import dev.rollczi.litecommands.argument.NotRequiredArgumentHandler;
import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.component.LiteExecution;
import dev.rollczi.litecommands.component.LiteSection;
import panda.std.Option;
import panda.std.stream.PandaStream;
import panda.utilities.StringUtils;
import panda.utilities.text.Joiner;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FunctionalInterface
public interface LiteMessage extends ContextualMessage {

    String message(MessageInfoContext messageInfoContext);

    @Override
    default String message(ExecutionResult result, UseSchemeFormatting formatting) {
        List<LiteComponent> resolvers = result.getLastContext().getTracesOfResolvers();
        StringBuilder builder = new StringBuilder();

        List<LiteComponent> withOutEmptyComponent = PandaStream.of(resolvers)
                .filterNot(component -> component.getScope().getName().isEmpty())
                .toList();

        String commandScheme = Joiner.on(" ")
                .join(withOutEmptyComponent, (index, component) -> index == 0
                        ? formatting.commandFormat().apply(component.getScope().getName())
                        : formatting.subcommandFormat().apply(component.getScope().getName()))
                .toString();

        builder.append(commandScheme);

        Option<LiteComponent> lastComponent = PandaStream.of(resolvers)
                .last();

        List<String> nextSubcommands = lastComponent
                .is(LiteSection.class)
                .toStream()
                .flatMap(LiteSection::getResolvers)
                .map(liteComponent -> liteComponent.getScope().getName())
                .collect(Collectors.toList());

        if (!nextSubcommands.isEmpty()) {
            builder.append(" ").append(formatting.nextSubcommandsFormat().apply(nextSubcommands));
        }

        String paramsScheme = lastComponent
                .is(LiteExecution.class)
                .map(LiteExecution::getExecutor)
                .toStream()
                .flatMap(executor -> executor.getArgumentHandlers().entrySet())
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .map(handler -> handler instanceof NotRequiredArgumentHandler
                        ? formatting.optionalParameterFormat().apply(handler.getName())
                        : formatting.parameterFormat().apply(handler.getName()))
                .collect(Collectors.joining(" "));

        builder.append(paramsScheme.isEmpty() ? StringUtils.EMPTY : " " + paramsScheme);

        String useScheme = formatting.commandSlash() + builder;

        MessageInfoContext messageInfoContext = new MessageInfoContext(result, useScheme);

        return message(messageInfoContext);
    }

}
