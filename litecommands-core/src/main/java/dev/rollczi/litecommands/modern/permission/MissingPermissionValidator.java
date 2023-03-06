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
        List<String> permissions = new ArrayList<>();

        CommandRouteUtils.consumeFromRootToChild(command, route -> {
            permissions.addAll(route.getMeta().get(CommandMeta.PERMISSIONS));
            permissions.removeAll(route.getMeta().get(CommandMeta.PERMISSIONS_EXCLUDED));
        });

        permissions.addAll(executor.getMeta().get(CommandMeta.PERMISSIONS));
        permissions.removeAll(executor.getMeta().get(CommandMeta.PERMISSIONS_EXCLUDED));

        MissingPermissions missingPermissions = MissingPermissions.check(invocation.getPlatformSender(), permissions);

        if (missingPermissions.isMissing()) {
            return CommandValidatorResult.invalid(missingPermissions, false);
        }

        return CommandValidatorResult.valid();
    }

}
