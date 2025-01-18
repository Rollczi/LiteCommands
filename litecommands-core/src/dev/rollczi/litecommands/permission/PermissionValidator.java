package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.validator.Validator;

public class PermissionValidator<SENDER> implements Validator<SENDER> {
    private final PermissionValidationService validator;

    public PermissionValidator(PermissionValidationService validator) {
        this.validator = validator;
    }

    @Override
    public Flow validate(Invocation<SENDER> invocation, MetaHolder metaHolder) {
        PermissionValidationResult result = this.validator.validate(metaHolder, invocation.platformSender());

        if (!result.isPermitted()) {
            return Flow.terminateFlow(new MissingPermissions(result.getVerdicts()));
        }

        return Flow.continueFlow();
    }

}
