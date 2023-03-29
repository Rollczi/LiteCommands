package dev.rollczi.litecommands.editor;

public interface CommandEditorContextProvider<SENDER> {

    CommandEditorContext<SENDER> getContext();

}
