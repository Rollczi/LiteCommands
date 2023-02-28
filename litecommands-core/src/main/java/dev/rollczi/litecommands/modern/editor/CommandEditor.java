package dev.rollczi.litecommands.modern.editor;

public interface CommandEditor<SENDER> {

    CommandEditorContext<SENDER> edit(CommandEditorContext<SENDER> context);

}
