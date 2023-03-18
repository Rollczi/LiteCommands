package dev.rollczi.litecommands.modern.annotation.editor;

import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.annotation.route.Route;
import dev.rollczi.litecommands.modern.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.test.FakeSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationCommandEditorServiceTest {

    private final static String KEY = "key";

    @Route(name = "test")
    @Editor(key = KEY)
    public static class Command {

        @Execute
        public void execute() {}

    }

    @Test
    void test() {
        AnnotationCommandEditorService<FakeSender> editorService = new AnnotationCommandEditorService<>();
        editorService.registerEditorKey(KEY, (context) -> context.name("prefix-" + context.name()));

        Command command = new Command();
        CommandEditorContext<FakeSender> oldContext = CommandEditorContext.<FakeSender>create().name("test");
        CommandEditorContext<FakeSender> context = editorService.edit(command, oldContext);

        assertEquals("prefix-test", context.name());
    }

}