package dev.rollczi.litecommands.argument.basictype.time;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.InvalidUsage;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Result;

import java.time.ZoneId;
import java.util.List;

public class ZoneIdArgument implements OneArgument<ZoneId> {

    @Override
    public Result<ZoneId, InvalidUsage> parse(LiteInvocation invocation, String argument) {
        return Result.supplyThrowing(() -> ZoneId.of(argument))
            .mapErr(exception -> InvalidUsage.INSTANCE);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Suggestion.of(ZoneId.getAvailableZoneIds());
    }

}