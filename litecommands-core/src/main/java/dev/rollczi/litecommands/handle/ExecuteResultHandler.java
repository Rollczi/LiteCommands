package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.scheme.SchemeFormat;
import dev.rollczi.litecommands.scheme.SchemeGenerator;
import dev.rollczi.litecommands.shared.MapUtil;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ExecuteResultHandler<SENDER> {

    private final SchemeGenerator schemeGenerator = SchemeGenerator.simple();
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

        if (object instanceof CompletableFuture<?>) {
            CompletableFuture<?> future = ((CompletableFuture<?>) object);

            future.whenComplete((o, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }

                this.handleResult(sender, invocation, result);
            });

            return;
        }


        this.handleResult(sender, invocation, object);
    }

    private void handleResult(SENDER sender, LiteInvocation invocation, Object object) {
        Class<?> type = object.getClass();
        Option<Handler<SENDER, ?>> handlerOpt = MapUtil.findSuperTypeOf(type, this.handlers);

        if (handlerOpt.isEmpty()) {
            Redirector<?, ?> forwarding = this.redirectors.get(type);

            if (forwarding == null) {
                throw new IllegalStateException("Missing result handler for type " + type);
            }

            Object to = handleRedirector(forwarding, object);
            Handler<SENDER, ?> handler = MapUtil.findSuperTypeOf(to.getClass(), this.handlers)
                    .orThrow(() -> new IllegalStateException("Missing result handler for type " + type + " or for redirected type " + to.getClass()));

            this.handleHandler(handler, sender, invocation, to);
            return;
        }

        this.handleHandler(handlerOpt.get(), sender, invocation, object);
    }

    @SuppressWarnings("unchecked")
    private <FROM, TO> TO handleRedirector(Redirector<FROM, TO> redirector, Object object) {
        return redirector.redirect((FROM) object);
    }

    @SuppressWarnings("unchecked")
    private <T> void handleHandler(Handler<SENDER, T> handler, SENDER sender, LiteInvocation invocation, Object result) {
        handler.handle(sender, invocation, (T) result);
    }

    public <FROM, TO> void registerRedirector(Class<FROM> from, Class<TO> to, Redirector<FROM, TO> redirector) {
        this.redirectors.put(from, redirector);
    }

    public <T> void registerHandler(Class<T> type, Handler<SENDER, T> handler) {
        this.handlers.put(type, handler);
    }

}
