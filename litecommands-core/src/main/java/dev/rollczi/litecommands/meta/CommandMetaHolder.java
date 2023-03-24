package dev.rollczi.litecommands.meta;

import java.util.function.Consumer;

public interface CommandMetaHolder {

    void editMeta(Consumer<CommandMeta> operator);

}
