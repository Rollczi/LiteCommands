package dev.rollczi.litecommands.invalid;

import dev.rollczi.litecommands.command.CommandExecuteResultHandler;

public interface InvalidUsageHandler<SENDER> extends CommandExecuteResultHandler<SENDER, InvalidUsage<SENDER>> {
}
