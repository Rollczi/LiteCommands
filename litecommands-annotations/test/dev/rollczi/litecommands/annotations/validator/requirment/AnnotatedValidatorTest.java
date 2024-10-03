package dev.rollczi.litecommands.annotations.validator.requirment;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.validator.ValidatorResult;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

class AnnotatedValidatorTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder
        .annotations(extension -> extension
            .validator(String.class, IsStupid.class, new IsStupidValidator())
        );

    @Retention(RetentionPolicy.RUNTIME)
    @interface IsStupid {
    }

    static class IsStupidValidator implements AnnotatedValidator<TestSender, String, IsStupid> {

        @Override
        public ValidatorResult validate(Invocation<TestSender> invocation, CommandExecutor<TestSender> executor, Requirement<String> requirement, String value, IsStupid annotation) {
            if (value.equals("stupid")) {
                return ValidatorResult.invalid("You are stupid!");
            }

            return ValidatorResult.valid();
        }
    }

    @Command(name = "command")
    static class TestCommand {
        @Execute
        public String execute(@Arg @IsStupid String arg) {
            return arg;
        }
    }

    @Test
    void testFailureValidation() {
        platform.execute("command stupid")
            .assertFailure("You are stupid!");
    }

    @Test
    void testSuccessValidation() {
        platform.execute("command not-stupid")
            .assertSuccess("not-stupid");
    }

}