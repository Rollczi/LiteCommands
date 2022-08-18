package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.schematic.Schematic;

public interface InvalidUsageHandler<SENDER> extends Handler<SENDER, Schematic> {

    void handle(SENDER sender, LiteInvocation invocation, Schematic schematic);

}
