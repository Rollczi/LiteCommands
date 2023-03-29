package dev.rollczi.litecommands.annotations.editor;

import dev.rollczi.litecommands.annotations.editor.AnnotationCommandEditorService;
import dev.rollczi.litecommands.annotations.editor.Editor;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.route.Route;
import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationCommandEditorServiceTest {

    private final static String KEY = "key";

    @Route(name = "test")
    @Editor(key = KEY)
    static class Command {

        @Execute
        void execute() {}

    }

    @Test
    void test() {
        AnnotationCommandEditorService<TestSender> editorService = new AnnotationCommandEditorService<>();
        editorService.registerEditorKey(KEY, (context) -> context.name("prefix-" + context.name()));

        Command command = new Command();
        CommandEditorContext<TestSender> oldContext = CommandEditorContext.<TestSender>create().name("test");
        CommandEditorContext<TestSender> context = editorService.edit(command, oldContext);

        assertEquals("prefix-test", context.name());
    }

}