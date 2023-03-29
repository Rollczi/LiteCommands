package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.CommandRouteUtils;
import dev.rollczi.litecommands.meta.CommandMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandValidatorService<SENDER> {

    private final Map<Class<?>, CommandValidator<SENDER>> commandValidators = new HashMap<>();
    private final List<CommandValidator<SENDER>> commandGlobalValidators = new ArrayList<>();

    public void registerValidator(CommandValidator<SENDER> commandValidator) {
        this.commandValidators.put(commandValidator.getClass(), commandValidator);
    }

    public void registerGlobalValidator(CommandValidator<SENDER> commandValidator) {
        this.commandGlobalValidators.add(commandValidator);
    }

    public CommandValidatorResult validate(Invocation<SENDER> invocation, CommandRoute<SENDER> commandRoute, CommandExecutor<SENDER> commandExecutor) {
        boolean invalid = false;

        List<Class<?>> validators = CommandRouteUtils.collectFromRootToExecutor(commandRoute, commandExecutor, commandMeta -> commandMeta.get(CommandMeta.VALIDATORS))
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        for (CommandValidator<SENDER> commandValidator : commandGlobalValidators) {
            CommandValidatorResult result = commandValidator.validate(invocation, commandRoute, commandExecutor);

            if (result.isInvalid()) {
                if (result.canBeIgnored() || result.hasInvalidResult()) {
                    return result;
                }

                invalid = true;
            }
        }

        for (Class<?> validator : validators) {
            CommandValidator<SENDER> commandValidator = this.commandValidators.get(validator);

            if (commandValidator == null) {
                throw new IllegalStateException("Validator " + validator + " not found");
            }

            CommandValidatorResult result = commandValidator.validate(invocation, commandRoute, commandExecutor);

            if (result.isInvalid()) {
                if (result.canBeIgnored() || result.hasInvalidResult()) {
                    return result;
                }

                invalid = true;
            }
        }

        return invalid ? CommandValidatorResult.invalid(false) : CommandValidatorResult.valid();
    }

}
