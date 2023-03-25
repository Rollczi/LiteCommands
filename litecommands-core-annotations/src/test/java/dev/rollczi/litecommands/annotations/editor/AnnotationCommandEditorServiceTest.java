package dev.rollczi.litecommands.annotations.editor;

import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.route.Route;
import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.test.FakeSender;
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