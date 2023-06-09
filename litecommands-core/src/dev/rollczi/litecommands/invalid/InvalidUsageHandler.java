package dev.rollczi.litecommands.invalid;

import dev.rollczi.litecommands.result.ResultHandler;

public interface InvalidUsageHandler<SENDER> extends ResultHandler<SENDER, InvalidUsage<SENDER>> {
}
