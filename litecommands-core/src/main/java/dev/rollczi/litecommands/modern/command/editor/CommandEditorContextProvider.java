package dev.rollczi.litecommands.modern.command.editor;

public interface CommandEditorContextProvider<SENDER> {

    CommandEditorContext<SENDER> getContext();

}
