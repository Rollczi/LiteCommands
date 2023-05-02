package dev.rollczi.litecommands.annotations.validator;

import dev.rollczi.litecommands.annotations.LiteTest;
import dev.rollczi.litecommands.annotations.LiteConfigurator;
import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.route.Route;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.unit.AssertExecute;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.validator.Validator;
import dev.rollczi.litecommands.validator.ValidatorResult;
import org.junit.jupiter.api.Test;

@LiteTest
class CustomValidatorTest extends LiteTestSpec {

    static class ValidValidator implements Validator<TestSender> {
        @Override
        public ValidatorResult validate(Invocation<TestSender> invocation, CommandRoute<TestSender> command, CommandExecutor<TestSender> executor) {
            return ValidatorResult.valid();
        }
    }

    static class InvalidValidator implements Validator<TestSender> {
        @Override
        public ValidatorResult validate(Invocation<TestSender> invocation, CommandRoute<TestSender> command, CommandExecutor<TestSender> executor) {
            return ValidatorResult.invalid(false);
        }
    }

    static class InvalidCanBeIgnoredValidator implements Validator<TestSender> {
        @Override
        public ValidatorResult validate(Invocation<TestSender> invocation, CommandRoute<TestSender> command, CommandExecutor<TestSender> executor) {
            return ValidatorResult.invalid(true);
        }
    }

    @LiteConfigurator
    static LiteConfig configurator() {
        return builder -> builder
            .withValidator(new ValidValidator())
            .withValidator(new InvalidValidator())
            .withValidator(new InvalidCanBeIgnoredValidator());
    }

    @Route(name = "command")
    @Validate(ValidValidator.class)
    static class TestCommand {

        @Execute(route = "none")
        public void none() {}

        @Execute(route = "invalid")
        @Validate(InvalidValidator.class)
        public void invalid() {}

        @Execute(route = "invalid-can-be-ignored")
        @Validate(InvalidCanBeIgnoredValidator.class)
        public void invalidCanBeIgnored() {}

    }

    @Test
    void shouldValidatePositiveValidation() {
        platform.execute("command none")
            .assertSuccess();
    }

    @Test
    void shouldValidateNegativeValidation() {
        platform.execute("command invalid")
            .assertFailure();
    }

    @Test
    void shouldValidateNegativeValidationCanBeIgnored() {
        platform.execute("command invalid-can-be-ignored")
            .assertFailure(InvalidUsage.Cause.UNKNOWN_COMMAND);
    }

    private void shouldValidate(String command, boolean expected) {
        AssertExecute assertExecute = platform.execute(command);

        if (expected) {
            assertExecute.assertSuccess();
        } else {
            assertExecute.assertFailure();
        }
    }

}
