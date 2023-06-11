package dev.rollczi.litecommands.annotations.validator;

import dev.rollczi.litecommands.annotations.LiteConfigurator;
import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.unit.AssertExecute;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.validator.Validator;
import org.junit.jupiter.api.Test;

class CustomValidatorTest extends LiteTestSpec {

    static class ValidValidator implements Validator<TestSender> {
        @Override
        public Flow validate(Invocation<TestSender> invocation, CommandRoute<TestSender> command, CommandExecutor<TestSender> executor) {
            return Flow.continueFlow();
        }
    }

    static class InvalidValidator implements Validator<TestSender> {
        @Override
        public Flow validate(Invocation<TestSender> invocation, CommandRoute<TestSender> command, CommandExecutor<TestSender> executor) {
            return Flow.terminateFlow("invalid");
        }
    }

    static class InvalidCanBeIgnoredValidator implements Validator<TestSender> {
        @Override
        public Flow validate(Invocation<TestSender> invocation, CommandRoute<TestSender> command, CommandExecutor<TestSender> executor) {
            return Flow.stopCurrentFlow("invalid-can-be-ignored");
        }
    }

    @LiteConfigurator
    static LiteConfig configurator() {
        return builder -> builder
            .validatorMarked(new ValidValidator())
            .validatorMarked(new InvalidValidator())
            .validatorMarked(new InvalidCanBeIgnoredValidator());
    }

    @Command(name = "command")
    @Validate(ValidValidator.class)
    static class TestCommand {

        @Execute(name = "none")
        public void none() {}

        @Execute(name = "invalid")
        @Validate(InvalidValidator.class)
        public void invalid() {}

        @Execute(name = "invalid-can-be-ignored")
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
            .assertFailure("invalid");
    }

    @Test
    void shouldValidateNegativeValidationCanBeIgnored() {
        platform.execute("command invalid-can-be-ignored")
            .assertFailure("invalid-can-be-ignored");
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
