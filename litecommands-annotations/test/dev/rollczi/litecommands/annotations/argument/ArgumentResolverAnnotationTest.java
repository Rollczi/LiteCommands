package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandManager;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArgumentResolverAnnotationTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        void execute(@Arg String test, @Arg Date myDate, @Arg("other") Date date) {}

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

        List<? extends Argument<?>> requirements = executor.getArguments();

        Argument<?> first = requirements.get(0);
        Argument<?> second = requirements.get(1);
        Argument<?> third = requirements.get(2);

        assertEquals("test", first.getName());
        assertEquals("myDate", second.getName());
        assertEquals("other", third.getName());
    }

}
