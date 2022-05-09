package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;

import java.util.HashMap;
import java.util.Map;

public class ExecuteResultHandler<SENDER> {

    private final Map<Class<?>, Handler<SENDER, ?>> handlers = new HashMap<>();

    public void handle(SENDER sender, LiteInvocation invocation, ExecuteResult result) {
        if (result.isFailure()) {
            //TODO: use message

            return;
        }

        Object object = result.getResult();

        if (object == null) {
            return;
        }

        Handler<SENDER, ?> handler = handlers.get(object.getClass());

        if (handler == null) {
            for (Map.Entry<Class<?>, Handler<SENDER, ?>> entry : handlers.entrySet()) {
                if (entry.getKey().isAssignableFrom(object.getClass())) {
                    handler = entry.getValue();
                    break;
                }
            }

            if (handler == null) {
                throw new IllegalArgumentException("No handler found for " + object.getClass());
            }
        }

        this.handle(handler, sender, invocation, object);
    }

    @SuppressWarnings("unchecked")
    private <T> void handle(Handler<SENDER, T> handler, SENDER sender, LiteInvocation invocation, Object result) {
        handler.handle(sender, invocation, (T) result);
    }

    public <T> void register(Class<T> type, Handler<SENDER, T> handler) {
        this.handlers.put(type, handler);
    }

}
