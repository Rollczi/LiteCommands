package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.scope.Scopeable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidatorService<SENDER> {

    private final Map<Scope, Set<Validator<SENDER>>> commandValidators = new HashMap<>();
    private final Map<Class<?>, Validator<SENDER>> validatorsByClass = new HashMap<>();
    private final List<Validator<SENDER>> commandGlobalValidators = new ArrayList<>();

    public void registerValidatorGlobal(Validator<SENDER> validator) {
        this.commandGlobalValidators.add(validator);
    }

    public void registerValidator(Scope scope, Validator<SENDER> validator) {
        if (Scope.GLOBAL_SCOPE.equals(scope)) {
            commandGlobalValidators.add(validator);
            return;
        }

        if (scope instanceof ValidatorScope) {
            ValidatorScope validatorScope = (ValidatorScope) scope;

            validatorsByClass.put(validatorScope.getType(), validator);
            return;
        }

        commandValidators.computeIfAbsent(scope, key -> new HashSet<>()).add(validator);
    }

    /* Kinda shitty, but I don't know how to do it better without losing performance https://github.com/Rollczi/LiteCommands/commit/3ac889d82e3e4d39fea27eee91cf5b01adacb412 */
    public Flow validate(Invocation<SENDER> invocation, Scopeable scopeable) {
        Flow lastStopped = null;

        for (Validator<SENDER> validator : commandGlobalValidators) {
            Flow flow = validator.validate(invocation, scopeable);

            switch (flow.status()) {
                case CONTINUE: continue;
                case TERMINATE: return flow;
                case STOP_CURRENT: lastStopped = flow;
            }
        }

        for (List<Class<? extends Validator<?>>> validators : scopeable.metaCollector().iterable(Meta.VALIDATORS)) {
            for (Class<? extends Validator<?>> validatorType : validators) {
                Validator<SENDER> validator = validatorsByClass.get(validatorType);

                if (validator == null) {
                    throw new IllegalStateException("Validator " + validatorType + " not found");
                }

                Flow flow = validator.validate(invocation, scopeable);

                switch (flow.status()) {
                    case CONTINUE: continue;
                    case TERMINATE: return flow;
                    case STOP_CURRENT: lastStopped = flow;
                }
            }
        }

        for (Map.Entry<Scope, Set<Validator<SENDER>>> entry : commandValidators.entrySet()) {
            if (!entry.getKey().isApplicable(scopeable)) {
                continue;
            }

            for (Validator<SENDER> validator : entry.getValue()) {
                Flow flow = validator.validate(invocation, scopeable);

                switch (flow.status()) {
                    case CONTINUE: continue;
                    case TERMINATE: return flow;
                    case STOP_CURRENT: lastStopped = flow;
                }
            }
        }

        if (lastStopped != null) {
            return lastStopped;
        }

        return Flow.continueFlow();
    }

}
