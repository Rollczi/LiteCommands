package dev.rollczi.litecommands.handler.result;

import dev.rollczi.litecommands.exception.LiteCommandsException;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

class ResultHandlerChainImpl<SENDER> implements ResultHandlerChain<SENDER> {

    private final ResultHandleService<SENDER> service;
    private final Set<Class<?>> handledTypes = new LinkedHashSet<>();

    public ResultHandlerChainImpl(ResultHandleService<SENDER> service) {
        this.service = service;
    }

    @Override
    public <T> void resolve(Invocation<SENDER> invocation, T result) {
        this.handledTypes.add(result.getClass());

        try {
            this.service.resolve(invocation, result, this);
        }
        catch (StackOverflowError error) {
            String handledTypes = this.handledTypes.stream()
                    .map(handlerType -> handlerType.getName())
                    .collect(Collectors.joining(", "));

            throw new LiteCommandsException("Cycle detected in result handlers: " + handledTypes);
        }
    }

}
