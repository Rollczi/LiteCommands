package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.annotations.validator.method.MethodValidator;
import dev.rollczi.litecommands.annotations.validator.method.MethodValidatorContext;
import dev.rollczi.litecommands.validator.ValidatorResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        List<JakartaRawResult.Entry> violationsList = violations.stream()
            .map(objectConstraintViolation -> {
                if (!(objectConstraintViolation.getPropertyPath() instanceof PathImpl)) {
                    throw new IllegalStateException("Invalid property path type: " + objectConstraintViolation.getPropertyPath().getClass());
                }

                PathImpl path = (PathImpl) objectConstraintViolation.getPropertyPath();
                NodeImpl leafNode = path.getLeafNode();

                if (leafNode.getKind() != ElementKind.PARAMETER) {
                    throw new IllegalStateException("Invalid leaf node kind: " + leafNode.getKind());
                }

                return new JakartaRawResult.Entry(objectConstraintViolation, context.getDefinition().getRequirement(leafNode.getParameterIndex()));
            }).collect(Collectors.toList());

        return ValidatorResult.invalid(new JakartaRawResult(violationsList));

    }

}
