package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.scheme.SchemeFormat;
import dev.rollczi.litecommands.scheme.SchemeGenerator;
import dev.rollczi.litecommands.shared.MapUtil;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;

public class ExecuteResultHandler<SENDER> {

    private final SchemeGenerator schemeGenerator = new SchemeGenerator();
    private final Map<Class<?>, Handler<SENDER, ?>> handlers = new HashMap<>();
    private final Map<Class<?>, Redirector<?, ?>> redirectors = new HashMap<>();

    private SchemeFormat schemeFormat = SchemeFormat.ARGUMENT_ANGLED_OPTIONAL_SQUARE;

    public void schemeFormat(SchemeFormat schemeFormat) {
        this.schemeFormat = schemeFormat;
    }

    public SchemeFormat schemeFormat() {
        return schemeFormat;
    }

    public void handle(SENDER sender, LiteInvocation invocation, ExecuteResult result) {
        Object object = result.getResult();

        if (object == null) {
            if (!result.isFailure()) {
                return;
            }

            object = this.schemeGenerator.generateScheme(result.getBased(), schemeFormat);
        }

        Class<?> type = object.getClass();
        Option<Handler<SENDER, ?>> handlerOpt = MapUtil.findByAssignableFromKey(type, this.handlers);

        if (handlerOpt.isEmpty()) {
            Redirector<?, ?> forwarding = this.redirectors.get(type);

            if (forwarding == null) {
                throw new IllegalStateException("Missing result handler for type " + type);
            }

            Object to = handleForwarding(forwarding, object);
            Handler<SENDER, ?> handler = MapUtil.findByAssignableFromKey(to.getClass(), this.handlers)
                    .orThrow(() -> new IllegalStateException("Missing result handler for type " + type + " or for redirected type " + to.getClass()));

            this.handle(handler, sender, invocation, to);
            return;
        }

        this.handle(handlerOpt.get(), sender, invocation, object);
    }

    @SuppressWarnings("unchecked")
    private <FROM, TO> TO handleForwarding(Redirector<FROM, TO> redirector, Object object) {
        return redirector.redirect((FROM) object);
    }

    @SuppressWarnings("unchecked")
    private <T> void handle(Handler<SENDER, T> handler, SENDER sender, LiteInvocation invocation, Object result) {
        handler.handle(sender, invocation, (T) result);
    }

    public <FROM, TO> void redirector(Class<FROM> from, Class<TO> to, Redirector<FROM, TO> redirector) {
        this.redirectors.put(from, redirector);
    }

    public <T> void register(Class<T> type, Handler<SENDER, T> handler) {
        this.handlers.put(type, handler);
    }

}
