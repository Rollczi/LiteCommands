package dev.rollczi.litecommands.annotations.validator.requirment;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.validator.ValidatorResult;
import dev.rollczi.litecommands.validator.requirement.RequirementValidator;

import java.lang.annotation.Annotation;

@Deprecated
class RequirementAnnotatedValidatorImpl<SENDER, T, A extends Annotation> implements RequirementValidator<SENDER, T> {

    private final AnnotatedValidator<SENDER, T, A> validator;
    private final A annotation;

    public RequirementAnnotatedValidatorImpl(AnnotatedValidator<SENDER, T, A> validator, A annotation) {
        this.validator = validator;
        this.annotation = annotation;
    }

    public ValidatorResult validate(Invocation<SENDER> invocation, CommandExecutor<SENDER> executor, Requirement<T> requirement, T value) {
        return validator.validate(invocation, executor, requirement, value, annotation);
    }

}
