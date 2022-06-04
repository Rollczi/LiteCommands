package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.scheme.Scheme;

public interface InvalidUsageHandler<SENDER> extends Handler<SENDER, Scheme> {

    void handle(SENDER sender, LiteInvocation invocation, Scheme scheme);

}
