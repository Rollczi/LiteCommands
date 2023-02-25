package dev.rollczi.litecommands.modern.command.editor;

public interface CommandEditor<SENDER> {

    CommandEditorContext<SENDER> edit(CommandEditorContext<SENDER> context);

}
