package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.validator.CommandValidator;
import dev.rollczi.litecommands.validator.CommandValidatorResult;

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
