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

public class ValidatorService<SENDER> {

    private final Map<Class<?>, Validator<SENDER>> commandValidators = new HashMap<>();
    private final List<Validator<SENDER>> commandGlobalValidators = new ArrayList<>();

    public void registerValidator(Validator<SENDER> validator, ValidatorScope scope) {
        switch (scope) {
            case GLOBAL:
                this.commandGlobalValidators.add(validator);
                break;
            case MARKED_META:
                this.commandValidators.put(validator.getClass(), validator);
                break;
        }
    }

    public ValidatorResult validate(Invocation<SENDER> invocation, CommandRoute<SENDER> commandRoute, CommandExecutor<SENDER> commandExecutor) {
        boolean invalid = false;

        List<Class<?>> validators = CommandRouteUtils.collectFromRootToExecutor(commandRoute, commandExecutor, commandMeta -> commandMeta.get(CommandMeta.VALIDATORS))
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        for (Validator<SENDER> validator : commandGlobalValidators) {
            ValidatorResult result = validator.validate(invocation, commandRoute, commandExecutor);

            if (result.isInvalid()) {
                if (result.canBeIgnored() || result.hasInvalidResult()) {
                    return result;
                }

                invalid = true;
            }
        }

        for (Class<?> validator : validators) {
            Validator<SENDER> liteValidator = this.commandValidators.get(validator);

            if (liteValidator == null) {
                throw new IllegalStateException("Validator " + validator + " not found");
            }

            ValidatorResult result = liteValidator.validate(invocation, commandRoute, commandExecutor);

            if (result.isInvalid()) {
                if (result.canBeIgnored() || result.hasInvalidResult()) {
                    return result;
                }

                invalid = true;
            }
        }

        return invalid ? ValidatorResult.invalid(false) : ValidatorResult.valid();
    }

}
