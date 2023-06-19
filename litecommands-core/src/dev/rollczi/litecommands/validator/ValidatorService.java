package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.CommandRouteUtils;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.util.IterableReferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidatorService<SENDER> {

    private final Map<Scope, Validator<SENDER>> commandValidators = new HashMap<>();
    private final Map<Class<?>, Validator<SENDER>> validatorsByClass = new HashMap<>();
    private final List<Validator<SENDER>> commandGlobalValidators = new ArrayList<>();

    public void registerValidatorGlobal(Validator<SENDER> validator) {
        this.commandGlobalValidators.add(validator);
    }

    public void registerValidator(Scope scope, Validator<SENDER> validator) {
        if (scope instanceof ValidatorScope) {
            ValidatorScope validatorScope = (ValidatorScope) scope;

            validatorsByClass.put(validatorScope.getType(), validator);
            return;
        }

        this.commandValidators.put(scope, validator);
    }

    public Flow validate(Invocation<SENDER> invocation, CommandRoute<SENDER> commandRoute, CommandExecutor<SENDER, ?> commandExecutor) {
        IterableReferences<Validator<SENDER>> validators = IterableReferences.of(
            () -> commandGlobalValidators,
            () -> fromMeta(commandRoute, commandExecutor),
            () -> fromScope(commandRoute)
        );

        return Flow.merge(validators, validator -> validator.validate(invocation, commandRoute, commandExecutor));
    }

    private List<Validator<SENDER>> fromMeta(CommandRoute<SENDER> commandRoute, CommandExecutor<SENDER, ?> commandExecutor) {
        List<Validator<SENDER>> validators = new ArrayList<>();

        List<Class<?>> validatorsTypes = CommandRouteUtils.collectFromRootToExecutor(commandRoute, commandExecutor, commandMeta -> commandMeta.get(CommandMeta.VALIDATORS))
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        for (Class<?> validatorType : validatorsTypes) {
            Validator<SENDER> validator = validatorsByClass.get(validatorType);

            if (validator == null) {
                throw new IllegalStateException("Validator " + validatorType + " not found");
            }

            validators.add(validator);
        }

        return validators;
    }

    private List<Validator<SENDER>> fromScope(CommandRoute<SENDER> commandRoute) {
        List<Validator<SENDER>> validators = new ArrayList<>();

        for (Map.Entry<Scope, Validator<SENDER>> entry : commandValidators.entrySet()) {
            Scope scope = entry.getKey();
            Validator<SENDER> validator = entry.getValue();

            if (!scope.isApplicable(commandRoute)) {
                continue;
            }

            validators.add(validator);
        }

        return validators;
    }


}
