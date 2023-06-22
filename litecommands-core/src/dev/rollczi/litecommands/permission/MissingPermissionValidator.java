package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class MissingPermissionValidator<SENDER> implements Validator<SENDER> {

    @Override
    public Flow validate(Invocation<SENDER> invocation, CommandRoute<SENDER> command, CommandExecutor<SENDER, ?> executor) {
        MissingPermissions missingPermissions = MissingPermissions.check(invocation.platformSender(), executor);

        if (missingPermissions.isMissing()) {
            return Flow.terminateFlow(missingPermissions);
        }

        return Flow.continueFlow();
    }

}
