package dev.rollczi.litecommands.annotations.validator.method;

import dev.rollczi.litecommands.validator.ValidatorResult;

import java.util.HashSet;
import java.util.Set;

public class MethodValidatorService<SENDER> {

    private final Set<MethodValidator<SENDER>> validators = new HashSet<>();

    public void register(MethodValidator<SENDER> validator) {
        validators.add(validator);
    }

    public ValidatorResult validate(MethodValidatorContext<SENDER> context) {
        for (MethodValidator<SENDER> validator : validators) {
            ValidatorResult result = validator.validate(context);
            if (result.isInvalid()) {
                return result;
            }
        }

        return ValidatorResult.valid();
    }

}
