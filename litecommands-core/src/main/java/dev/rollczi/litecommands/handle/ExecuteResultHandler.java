package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import dev.rollczi.litecommands.shared.MapUtil;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ExecuteResultHandler<SENDER> {

    private final Map<Class<?>, Handler<SENDER, ?>> handlers = new HashMap<>();
    private final Map<Class<?>, Redirector<?, ?>> redirectors = new HashMap<>();

    private SchematicGenerator schematicGenerator = SchematicGenerator.simple();
    private SchematicFormat schematicFormat = SchematicFormat.ARGUMENT_ANGLED_OPTIONAL_SQUARE;

    public SchematicGenerator getSchematicGenerator() {
        return schematicGenerator;
    }

    public void setSchematicGenerator(SchematicGenerator schematicGenerator) {
        this.schematicGenerator = schematicGenerator;
    }

    public void setSchematicFormat(SchematicFormat schematicFormat) {
        this.schematicFormat = schematicFormat;
    }

    public SchematicFormat getSchematicFormat() {
        return schematicFormat;
    }

    public void handle(SENDER sender, LiteInvocation invocation, ExecuteResult result) {
        Object object = result.getResult();

        if (object == null) {
            if (!result.isFailure()) {
                return;
            }

            object = this.schematicGenerator.generateSchematic(result.getBased(), schematicFormat);
        }

        if (object instanceof CompletableFuture<?>) {
            CompletableFuture<?> future = ((CompletableFuture<?>) object);

            future.whenComplete((obj, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }

                this.handleResult(sender, invocation, obj);
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
