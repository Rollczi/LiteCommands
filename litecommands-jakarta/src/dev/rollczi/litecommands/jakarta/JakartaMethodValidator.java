package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.annotations.validator.method.MethodValidator;
import dev.rollczi.litecommands.annotations.validator.method.MethodValidatorContext;
import dev.rollczi.litecommands.validator.ValidatorResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;

import java.util.Set;

class JakartaMethodValidator<SENDER> implements MethodValidator<SENDER> {

    private final Validator validator;

    public JakartaMethodValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public ValidatorResult validate(MethodValidatorContext<SENDER> context) {
        ExecutableValidator executableValidator = validator.forExecutables();
        Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(context.getCommand(), context.getMethod(), context.getArgs());

        if (violations.isEmpty()) {
            return ValidatorResult.valid();
        }

        return ValidatorResult.invalid(new JakartaResult(violations));
    }

}
