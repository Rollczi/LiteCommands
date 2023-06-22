package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.shared.IterableReferences;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
            commandGlobalValidators,
            new IterableMeta(commandExecutor),
            new IterableScope(commandRoute)
        );

        return Flow.merge(validators, validator -> validator.validate(invocation, commandRoute, commandExecutor));
    }

    private class IterableMeta implements Iterable<Validator<SENDER>> {
        private final CommandExecutor<SENDER, ?> commandExecutor;

        private IterableMeta(CommandExecutor<SENDER, ?> commandExecutor) {
            this.commandExecutor = commandExecutor;
        }

        @Override
        public @NotNull Iterator<Validator<SENDER>> iterator() {
            return commandExecutor.metaCollector().collect(Meta.VALIDATORS)
                .stream()
                .flatMap(List::stream)
                .map(validatorType -> {
                    Validator<SENDER> validator = validatorsByClass.get(validatorType);

                    if (validator == null) {
                        throw new IllegalStateException("Validator " + validatorType + " not found");
                    }

                    return validator;
                })
                .iterator();
        }
    }

    public class IterableScope implements Iterable<Validator<SENDER>> {
        private final CommandRoute<SENDER> commandRoute;

        private IterableScope(CommandRoute<SENDER> commandRoute) {
            this.commandRoute = commandRoute;
        }

        @Override
        public @NotNull Iterator<Validator<SENDER>> iterator() {
            return commandValidators.entrySet().stream()
                .filter(entry -> entry.getKey().isApplicable(commandRoute))
                .map(entry -> entry.getValue())
                .iterator();
        }
    }


}
