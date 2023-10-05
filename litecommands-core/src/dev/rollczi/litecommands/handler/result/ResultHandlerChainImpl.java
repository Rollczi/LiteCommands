package dev.rollczi.litecommands.handler.result;

import dev.rollczi.litecommands.LiteCommandsException;
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
    public <T> void resolve(Invocation<SENDER> invocation, T result, Class<? super T> typeAs) {
        this.handledTypes.add(typeAs);

        try {
            this.service.resolve(invocation, result, typeAs, this);
        }
        catch (StackOverflowError error) {
            String handledTypes = this.handledTypes.stream()
                .map(handlerType -> handlerType.getName())
                .collect(Collectors.joining(", "));

            throw new LiteCommandsException("Cycle detected in result handlers: " + handledTypes);
        }
    }

}
