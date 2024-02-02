package dev.rollczi.litecommands.annotations.validator.method;

import dev.rollczi.litecommands.validator.ValidatorResult;

public interface MethodValidator<SENDER> {

    ValidatorResult validate(MethodValidatorContext<SENDER> context);

}
