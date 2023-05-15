package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.InvalidUsage;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.schematic.Schematic;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import dev.rollczi.litecommands.shared.MapUtil;
import org.jetbrains.annotations.ApiStatus;
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
        return this.schematicGenerator;
    }

    public void setSchematicGenerator(SchematicGenerator schematicGenerator) {
        this.schematicGenerator = schematicGenerator;
    }

    public void setSchematicFormat(SchematicFormat schematicFormat) {
        this.schematicFormat = schematicFormat;
    }

    public SchematicFormat getSchematicFormat() {
        return this.schematicFormat;
    }

    public void handle(SENDER sender, LiteInvocation invocation, ExecuteResult executeResult) {
        this.handle(sender, invocation, executeResult, executeResult.getResult());
    }

    private void handle(SENDER sender, LiteInvocation invocation, ExecuteResult executeResult, Object object) {
        if (object == null) {
            if (!executeResult.isFailure()) {
                return;
            }

            Schematic schematic = this.schematicGenerator.generateSchematic(executeResult.getBased(), this.schematicFormat);

            this.handle(sender, invocation, executeResult, schematic);
            return;
        }

        if (object instanceof InvalidUsage) {
            Schematic schematic = this.schematicGenerator.generateSchematic(executeResult.getBased(), this.schematicFormat);

            this.handle(sender, invocation, executeResult, schematic);
            return;
        }

        if (object instanceof CompletableFuture<?>) {
            CompletableFuture<?> future = ((CompletableFuture<?>) object);

            future.whenComplete((obj, throwable) -> {
                try {
                    if (throwable != null) {
                        this.handle(sender, invocation, executeResult, throwable);
                        return;
                    }

                    if (obj != null) {
                        this.handle(sender, invocation, executeResult, obj);
                    }
                }
                catch (Throwable error) {
                    error.printStackTrace();
                }
            });

            return;
        }

        this.handleResult(sender, invocation, executeResult, object);
    }

    @ApiStatus.Internal
    public void handleResult(SENDER sender, LiteInvocation invocation, ExecuteResult executeResult, Object object) {
        Class<?> type = object.getClass();
        Option<Handler<SENDER, ?>> handlerOpt = MapUtil.findSuperTypeOf(type, this.handlers);

        if (handlerOpt.isEmpty()) {
            Redirector<?, ?> forwarding = this.redirectors.get(type);

            if (forwarding == null) {
                if (object instanceof RuntimeException) {
                    throw (RuntimeException) object;
                }

                if (object instanceof Throwable) {
                    throw new LiteCommandsExecutionException("Exception thrown during command execution", (Throwable) object);
                }

                throw new LiteCommandsExecutionException("Missing result handler for type " + type);
            }

            Object to = this.handleRedirector(forwarding, object);
            this.handle(sender, invocation, executeResult, to);
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
