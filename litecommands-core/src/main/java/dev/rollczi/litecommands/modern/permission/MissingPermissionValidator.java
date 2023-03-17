package dev.rollczi.litecommands.modern.permission;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.command.CommandRouteUtils;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.validator.CommandValidator;
import dev.rollczi.litecommands.modern.validator.CommandValidatorResult;

import java.util.ArrayList;
import java.util.List;

public class MissingPermissionValidator<SENDER> implements CommandValidator<SENDER> {

    @Override
    public CommandValidatorResult validate(Invocation<SENDER> invocation, CommandRoute<SENDER> command, CommandExecutor<SENDER> executor) {
        MissingPermissions missingPermissions = MissingPermissions.check(invocation.getPlatformSender(), command, executor);

        if (missingPermissions.isMissing()) {
            return CommandValidatorResult.invalid(missingPermissions, false);
        }

        return CommandValidatorResult.valid();
    }

}
