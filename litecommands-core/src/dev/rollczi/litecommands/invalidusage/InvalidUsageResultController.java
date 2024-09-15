package dev.rollczi.litecommands.invalidusage;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.event.CommandPostExecutionEvent;
import dev.rollczi.litecommands.event.EventListener;
import dev.rollczi.litecommands.event.Subscriber;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import dev.rollczi.litecommands.schematic.SchematicInput;
import org.jetbrains.annotations.Nullable;

public class InvalidUsageResultController<SENDER> implements EventListener {

    private final SchematicGenerator<SENDER> schematicGenerator;

    public InvalidUsageResultController(SchematicGenerator<SENDER> schematicGenerator) {
        this.schematicGenerator = schematicGenerator;
    }

    @Subscriber
    public void onEvent(CommandPostExecutionEvent<SENDER> event) {
        CommandExecutor<SENDER> executor = event.getExecutor();
        CommandRoute<SENDER> commandRoute = event.getCommandRoute();
        Invocation<SENDER> invocation = event.getInvocation();
        CommandExecuteResult executeResult = event.getResult();

        Object result = executeResult.getResult();
        if (result != null) {
            event.setResult(CommandExecuteResult.success(executor, mapResult(result, commandRoute, executeResult, invocation)));
        }

        Object error = executeResult.getError();
        if (error != null) {
            Object mapped = mapResult(error, commandRoute, executeResult, invocation);

            if (executor == null) {
                event.setResult(CommandExecuteResult.failed(mapped));
                return;
            }

            event.setResult(CommandExecuteResult.failed(executor, mapped));
        }
    }

    @SuppressWarnings("unchecked")
    private Object mapResult(Object error, CommandRoute<SENDER> commandRoute, CommandExecuteResult executeResult, Invocation<SENDER> invocation) {
        if (error instanceof InvalidUsage.Cause) {
            InvalidUsage.Cause cause = (InvalidUsage.Cause) error;
            @Nullable CommandExecutor<SENDER> executor = (CommandExecutor<SENDER>) executeResult.getExecutor();
            Schematic schematic = schematicGenerator.generate(new SchematicInput<>(commandRoute, executor, invocation));

            return new InvalidUsage<>(cause, commandRoute, schematic);
        }

        return error;
    }

}
