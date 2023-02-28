package dev.rollczi.litecommands.modern.editor;

public interface CommandEditorContextProvider<SENDER> {

    CommandEditorContext<SENDER> getContext();

}
