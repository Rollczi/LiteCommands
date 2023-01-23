package dev.rollczi.litecommands.modern.extension.annotation;

import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsInternalPattern;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.modern.extension.LiteExtension;
import dev.rollczi.litecommands.modern.extension.annotation.editor.AnnotationCommandEditorService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiteAnnotationExtension<SENDER> implements LiteExtension<SENDER> {

    private final List<Object> commandInstances = new ArrayList<>();
    private final List<Class<?>> commandClasses = new ArrayList<>();

    public LiteAnnotationExtension<SENDER> command(Object... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this;
    }

    public LiteAnnotationExtension<SENDER> command(Class<?>... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this;
    }

    public static <SENDER> LiteAnnotationExtension<SENDER> create() {
        return new LiteAnnotationExtension<>();
    }

    @Override
    public <B extends LiteCommandsBuilder<SENDER, B> & LiteCommandsInternalPattern<SENDER>> void extend(B builder) {
        AnnotationCommandEditorService service = new AnnotationCommandEditorService(builder.getCommandEditorService());
        CommandEditorContextRegistry contextRegistry = builder.getCommandContextRegistry();
    }

}