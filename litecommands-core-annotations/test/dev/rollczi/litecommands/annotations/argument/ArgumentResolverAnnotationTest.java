package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.Command;
import dev.rollczi.litecommands.command.executor.Execute;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandManager;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.requirement.ArgumentRequirement;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ArgumentResolverAnnotationTest extends LiteTestSpec {

    static LiteConfig config = builder -> builder
            .argumentSuggester(String.class, SuggestionResult.of("suggestion"));

    @Command(name = "test")
    static class TestCommand {

        @Execute
        void execute(@Arg String test, @Arg Date date, @Arg("other") Date other) {}

    }

    @Test
    @DisplayName("Should return correct argument names")
    void test() {
        CommandManager<TestSender> commandManager = liteCommands.getCommandManager();
        CommandRoute<TestSender> testChild = commandManager.getRoot().getChild("test")
            .orElseThrow(() -> new RuntimeException("Command not found"));

        CommandExecutor<TestSender> executor = testChild.getExecutors()
            .stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Executor not found"));

        List<? extends Requirement<TestSender, ?>> requirements = executor.getRequirements();

        ArgumentRequirement first = assertInstanceOf(ArgumentRequirement.class, requirements.get(0));
        ArgumentRequirement second = assertInstanceOf(ArgumentRequirement.class, requirements.get(1));
        ArgumentRequirement third = assertInstanceOf(ArgumentRequirement.class, requirements.get(2));

        assertEquals("arg0", first.getArgument().getName());
        assertEquals("arg1", second.getArgument().getName()); //  @ArgumentResolverInfo deprecated
        assertEquals("other", third.getArgument().getName());
    }

}
