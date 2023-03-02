package dev.rollczi.litecommands.modern.annotation.editor;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.modern.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.editor.CommandEditorService;
import dev.rollczi.litecommands.modern.env.FakeSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        AnnotationCommandEditorService<FakeSender> editorService = new AnnotationCommandEditorService<>(new CommandEditorService<>());
        editorService.registerEditorKey(KEY, (context) -> context.name("prefix-" + context.name()));

        Command command = new Command();
        CommandEditorContext<FakeSender> oldContext = CommandEditorContext.<FakeSender>create().name("test");
        CommandEditorContext<FakeSender> context = editorService.edit(command, oldContext);

        assertEquals("prefix-test", context.name());
    }

}