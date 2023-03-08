package dev.rollczi.litecommands.modern.meta;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface CommandMetaHolder {

    void editMeta(Consumer<CommandMeta> operator);

}
