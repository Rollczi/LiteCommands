package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.scheme.SchemeFormat;
import dev.rollczi.litecommands.scheme.SchemeGenerator;

import java.util.HashMap;
import java.util.Map;

public class ExecuteResultHandler<SENDER> {

    private final SchemeGenerator schemeGenerator = new SchemeGenerator();
    private final Map<Class<?>, Handler<SENDER, ?>> handlers = new HashMap<>();

    public void handle(SENDER sender, LiteInvocation invocation, ExecuteResult result) {
        if (result.isFailure()) {
            Handler<SENDER, ?> handler = handlers.get(String.class);

            handle(handler, sender, invocation, "Invalid usage: " + schemeGenerator.generate(result.getBased(), SchemeFormat.ARGUMENT_ANGLED_OPTIONAL_SQUARE));

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
