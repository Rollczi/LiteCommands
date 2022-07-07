package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import net.kyori.adventure.audience.Audience;
import panda.std.Result;

class KyoriAudienceContextual implements Contextual<CommandSource, Audience> {

    @Override
    public Result<Audience, Object> extract(CommandSource commandSender, Invocation<CommandSource> invocation) {
        return Result.ok(commandSender);
    }

}
