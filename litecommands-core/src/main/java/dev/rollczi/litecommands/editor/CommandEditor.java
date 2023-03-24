package dev.rollczi.litecommands.editor;

public interface CommandEditor<SENDER> {

    CommandEditorContext<SENDER> edit(CommandEditorContext<SENDER> context);

}
