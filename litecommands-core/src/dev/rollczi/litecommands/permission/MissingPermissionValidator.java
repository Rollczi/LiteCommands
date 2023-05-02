package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.validator.Validator;
import dev.rollczi.litecommands.validator.ValidatorResult;

public class MissingPermissionValidator<SENDER> implements Validator<SENDER> {

    @Override
    public ValidatorResult validate(Invocation<SENDER> invocation, CommandRoute<SENDER> command, CommandExecutor<SENDER> executor) {
        MissingPermissions missingPermissions = MissingPermissions.check(invocation.platformSender(), command, executor);

        if (missingPermissions.isMissing()) {
            return ValidatorResult.invalid(missingPermissions, false);
        }

        return ValidatorResult.valid();
    }

}
