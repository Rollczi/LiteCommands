package dev.rollczi.litecommands.valid.messages;

import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.component.LiteExecution;
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

        String commandScheme = Joiner.on(" ")
                .join(resolvers, (index, component) -> index == 0
                        ? formatting.commandFormat().apply(component.getScope().getName())
                        : formatting.subcommandFormat().apply(component.getScope().getName()))
                .toString();

        builder.append(commandScheme);

        String paramsScheme = PandaStream.of(resolvers)
                .last()
                .is(LiteExecution.class)
                .map(LiteExecution::getExecutor)
                .toStream()
                .flatMap(executor -> executor.getArgumentHandlers().entrySet())
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .map(argument -> argument.getName().orElseGet("none"))
                .map(formatting.parameterFormat())
                .collect(Collectors.joining(" "));

        builder.append(paramsScheme.isEmpty() ? StringUtils.EMPTY : " " + paramsScheme);

        String useScheme = formatting.commandSlash() + builder;

        MessageInfoContext messageInfoContext = new MessageInfoContext(result, useScheme);

        return message(messageInfoContext);
    }

}
