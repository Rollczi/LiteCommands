package dev.rollczi.litecommands.modern.annotation;

import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsCoreBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsInternalPattern;
import dev.rollczi.litecommands.modern.annotation.editor.AnnotationCommandEditorService;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContextRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiteCommandsAnnotationBuilderImpl<SENDER, B extends LiteCommandsAnnotationBuilderImpl<SENDER, B>> extends LiteCommandsCoreBuilder<SENDER, B> implements LiteCommandsAnnotationBuilder<SENDER, B> {

    private final List<Object> commandInstances = new ArrayList<>();
    private final List<Class<?>> commandClasses = new ArrayList<>();

    public LiteCommandsAnnotationBuilderImpl(Class<SENDER> senderClass) {
        super(senderClass);
    }

    @Override
    public B command(Object... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this.getThis();
    }

    @Override
    public B command(Class<?>... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this.getThis();
    }

    @Override
    public <B extends LiteCommandsBuilder<SENDER, B> & LiteCommandsInternalPattern<SENDER>> void extend(B builder) {
        AnnotationCommandEditorService service = new AnnotationCommandEditorService(builder.getCommandEditorService());
        CommandEditorContextRegistry contextRegistry = builder.getCommandContextRegistry();
    }

}