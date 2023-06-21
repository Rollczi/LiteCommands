package dev.rollczi.litecommands.guide;

import dev.rollczi.litecommands.handler.result.ResultMapper;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;

public class GuideMissingPermission<SENDER> implements ResultMapper<SENDER, MissingPermissions, String> {

    @Override
    public String map(Invocation<SENDER> invocation, MissingPermissions missingPermissions) {
        StringBuilder message = new StringBuilder();

        String permissions = missingPermissions.asJoinedText();

        message.append("You don't have permission to execute this command! (").append(permissions).append(")").append("\n");
        message.append("You can change this message by implementing MissingPermissionsHandler").append("\n");
        message.append("See https://docs.rollczi.dev/litecommands/???").append("\n"); // TODO add link

        return message.toString();
    }

}
