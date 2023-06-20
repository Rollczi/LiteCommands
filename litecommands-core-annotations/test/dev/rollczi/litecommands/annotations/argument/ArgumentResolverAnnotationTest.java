package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteConfigurator;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.argument.suggestion.SuggestionResult;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandManager;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.requirement.ArgumentRequirement;
import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ArgumentResolverAnnotationTest extends LiteTestSpec {

    @LiteConfigurator
    static LiteConfig config() {
        return builder -> builder
            .argumentSuggester(String.class, SuggestionResult.of("suggestion"))
            .argument(Date.class, new DateArgumentResolver());
    }


    @ArgumentResolverInfo(name = "custom")
    static class DateArgumentResolver extends ArgumentResolver<TestSender, Date> {
        @Override
        protected ParseResult<Date> parse(Invocation<TestSender> invocation, Argument<Date> context, String argument) {
            return null;
        }
    }

    @Command(name = "test")
    static class TestCommand {

        @Execute
        void execute(@Arg String test, @Arg Date date, @Arg("other") Date other) {}

    }

    @Test
    @DisplayName("Should return correct argument names")
    void test() {
        CommandManager<TestSender> commandManager = liteCommands.getCommandManager();
        CommandRoute<TestSender> testChild = commandManager.getRoot().getChildren("test")
            .orElseThrow(() -> new RuntimeException("Command not found"));

        CommandExecutor<TestSender, ?> executor = testChild.getExecutors()
            .stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Executor not found"));

        List<? extends Requirement<TestSender, ?>> requirements = executor.getRequirements();

        ArgumentRequirement first = assertInstanceOf(ArgumentRequirement.class, requirements.get(0));
        ArgumentRequirement second = assertInstanceOf(ArgumentRequirement.class, requirements.get(1));
        ArgumentRequirement third = assertInstanceOf(ArgumentRequirement.class, requirements.get(2));

        assertEquals("arg0", first.getArgument().getName());
        assertEquals("custom", second.getArgument().getName());
        assertEquals("other", third.getArgument().getName());
    }

}
