package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scope.Scope;

import java.util.ArrayList;
import java.util.HashMap;
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

    /* Kinda shitty, but I don't know how to do it better without losing performance https://github.com/Rollczi/LiteCommands/commit/3ac889d82e3e4d39fea27eee91cf5b01adacb412 */
    public Flow validate(Invocation<SENDER> invocation, CommandRoute<SENDER> commandRoute, CommandExecutor<SENDER, ?> commandExecutor) {
        Flow lastStopped = null;

        for (Validator<SENDER> validator : commandGlobalValidators) {
            Flow flow = validator.validate(invocation, commandRoute, commandExecutor);

            switch (flow.status()) {
                case CONTINUE: continue;
                case TERMINATE: return flow;
                case STOP_CURRENT: lastStopped = flow;
            }
        }

        for (List<Class<? extends Validator<?>>> validators : commandExecutor.metaCollector().iterable(Meta.VALIDATORS)) {
            for (Class<? extends Validator<?>> validatorType : validators) {
                Validator<SENDER> validator = validatorsByClass.get(validatorType);

                if (validator == null) {
                    throw new IllegalStateException("Validator " + validatorType + " not found");
                }

                Flow flow = validator.validate(invocation, commandRoute, commandExecutor);

                switch (flow.status()) {
                    case CONTINUE: continue;
                    case TERMINATE: return flow;
                    case STOP_CURRENT: lastStopped = flow;
                }
            }
        }

        for (Map.Entry<Scope, Validator<SENDER>> entry : commandValidators.entrySet()) {
            if (!entry.getKey().isApplicable(commandRoute)) {
                continue;
            }

            Flow flow = entry.getValue().validate(invocation, commandRoute, commandExecutor);

            switch (flow.status()) {
                case CONTINUE: continue;
                case TERMINATE: return flow;
                case STOP_CURRENT: lastStopped = flow;
            }
        }

        if (lastStopped != null) {
            return lastStopped;
        }

        return Flow.continueFlow();
    }

}
