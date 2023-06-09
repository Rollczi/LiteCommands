package dev.rollczi.litecommands.annotations.editor;

import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.route.Route;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationEditorServiceTest {

    private final static String KEY = "key";

    @Route(name = "test")
    @Edit(key = KEY)
    static class Command {

        @Execute
        void execute() {}

    }

    @Test
    void test() {
        AnnotationEditorService<TestSender> editorService = new AnnotationEditorService<>();
        editorService.registerEditorKey(KEY, (context) -> context.name("prefix-" + context.name()));

        Command command = new Command();
        CommandBuilder<TestSender> oldContext = CommandBuilder.<TestSender>create().name("test");
        CommandBuilder<TestSender> context = editorService.edit(command.getClass(), oldContext);

        assertEquals("prefix-test", context.name());
    }

}