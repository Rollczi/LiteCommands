package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.validator.Validator;

public class MissingPermissionValidator<SENDER> implements Validator<SENDER> {

    @Override
    public Flow validate(Invocation<SENDER> invocation, CommandRoute<SENDER> command, CommandExecutor<SENDER, ?> executor) {
        MissingPermissions missingPermissions = MissingPermissions.check(invocation.platformSender(), command, executor);

        if (missingPermissions.isMissing()) {
            return Flow.terminateFlow(missingPermissions);
        }

        return Flow.continueFlow();
    }

}
