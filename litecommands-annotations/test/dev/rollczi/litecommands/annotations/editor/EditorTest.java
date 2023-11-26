package dev.rollczi.litecommands.annotations.editor;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.programmatic.LiteCommand;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

// TODO move to framework test
class EditorTest extends LiteTestSpec {

    static LiteConfig config = builder -> builder
        .commands(new ProgrammaticToEdit())
        .editor(Scope.command(CommandToEdit.class), context -> context.name("after"))
        .editor(Scope.command(ProgrammaticToEdit.class), context -> context.name("p-after"));

    @Command(name = "before")
    static class CommandToEdit {
        @Execute
        void test() {}
    }

    static class ProgrammaticToEdit extends LiteCommand<TestSender> {

        public ProgrammaticToEdit() {
            super("p-before");
        }

    }

    @Test
    void test() {
        platform.execute("after")
            .assertSuccess();
    }

    @Test
    void testProgrammatic() {
        platform.execute("p-after")
            .assertSuccess();
    }

}
