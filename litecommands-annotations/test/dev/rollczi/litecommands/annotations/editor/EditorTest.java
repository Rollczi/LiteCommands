package dev.rollczi.litecommands.annotations.editor;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.programmatic.LiteCommand;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

// TODO move to framework test
class EditorTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder
        .commands(new ProgrammaticToEdit())
        .commands(new NamedProgrammaticToEdit())
        .editor(Scope.command(CommandToEdit.class), context -> context.name("class-after"))
        .editor(Scope.command(ProgrammaticToEdit.class), context -> context.name("programmatic-class-after"))
        .editor(Scope.command("named-before"), context -> context.name("named-after"))
        .editor(Scope.command("programmatic-named-before"), context -> context.name("programmatic-named-after"))
        .editor(Scope.command(RootCommandToEdit.class), context -> context.editChild("root-class-before", child -> child.name("root-class-after")))
        .editor(Scope.command("root-named-before"), context -> context.name("root-named-after"));


    @Command(name = "class-before")
    static class CommandToEdit {
        @Execute
        void test() {}
    }

    @Command(name = "named-before")
    static class NamedToEdit {
        @Execute
        void test() {}
    }

    @RootCommand
    static class RootCommandToEdit {
        @Execute(name = "root-class-before")
        void test() {}
    }

    @RootCommand
    static class RootNamedToEdit {
        @Execute(name = "root-named-before")
        void test() {}
    }

    static class ProgrammaticToEdit extends LiteCommand<TestSender> {

        public ProgrammaticToEdit() {
            super("programmatic-class-before");
        }

    }

    static class NamedProgrammaticToEdit extends LiteCommand<TestSender> {

        public NamedProgrammaticToEdit() {
            super("programmatic-named-before");
        }

    }

    @Test
    void test() {
        platform.execute("class-after")
            .assertSuccess();

        platform.execute("named-after")
            .assertSuccess();
    }

    @Test
    void testProgrammatic() {
        platform.execute("programmatic-class-after")
            .assertSuccess();

        platform.execute("programmatic-named-after")
            .assertSuccess();
    }

    @Test
    void testRoot() {
        platform.execute("root-class-after")
            .assertSuccess();

        platform.execute("root-named-after")
            .assertSuccess();
    }

}
