package dev.rollczi.litecommands.annotations.editor;

import dev.rollczi.litecommands.command.Command;
import dev.rollczi.litecommands.execute.Execute;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.edit.AnnotationEditorService;
import dev.rollczi.litecommands.edit.Edit;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationEditorServiceTest {

    private final static String KEY = "key";

    @Command(name = "test")
    @Edit(key = KEY)
    static class TestCommand {

        @Execute
        void execute() {}

    }

    @Test
    void test() {
        AnnotationEditorService<TestSender> editorService = new AnnotationEditorService<>();
        editorService.registerEditorKey(KEY, (context) -> context.name("prefix-" + context.name()));

        TestCommand command = new TestCommand();
        CommandBuilder<TestSender> oldContext = CommandBuilder.<TestSender>create().name("test");
        CommandBuilder<TestSender> context = editorService.edit(command.getClass(), oldContext);

        assertEquals("prefix-test", context.name());
    }

}