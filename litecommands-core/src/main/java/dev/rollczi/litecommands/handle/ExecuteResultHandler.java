package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.scheme.Scheme;
import dev.rollczi.litecommands.scheme.SchemeFormat;
import dev.rollczi.litecommands.scheme.SchemeGenerator;

import java.util.HashMap;
import java.util.Map;

public class ExecuteResultHandler<SENDER> {

    private final SchemeGenerator schemeGenerator = new SchemeGenerator();
    private final Map<Class<?>, Handler<SENDER, ?>> handlers = new HashMap<>();
    private SchemeFormat schemeFormat = SchemeFormat.ARGUMENT_ANGLED_OPTIONAL_SQUARE;

    public void schemeFormat(SchemeFormat schemeFormat) {
        this.schemeFormat = schemeFormat;
    }

    public SchemeFormat schemeFormat() {
        return schemeFormat;
    }

    public void handle(SENDER sender, LiteInvocation invocation, ExecuteResult result) {
        if (result.isFailure()) {
            Handler<SENDER, ?> handler = this.handlers.get(Scheme.class);
            Handler<SENDER, ?> stringHandler = this.handlers.get(String.class);

            if (handler == null) {
                if (stringHandler == null) {
                    return;
                }

                for (String scheme : this.schemeGenerator.generate(result.getBased(), schemeFormat)) {
                    this.handle(stringHandler, sender, invocation, scheme);
                }

                return;
            }

            Scheme scheme = this.schemeGenerator.generateScheme(result.getBased(), schemeFormat);

            this.handle(handler, sender, invocation, scheme);
            return;
        }

        Object object = result.getResult();

        if (object == null) {
            return;
        }

        Handler<SENDER, ?> handler = this.handlers.get(object.getClass());

        if (handler == null) {
            for (Map.Entry<Class<?>, Handler<SENDER, ?>> entry : this.handlers.entrySet()) {
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
