package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.strict.StrictService;
import dev.rollczi.litecommands.validator.Validator;

public class MissingPermissionValidator<SENDER> implements Validator<SENDER> {
    private final StrictService strictService;

    public MissingPermissionValidator(StrictService strictService) {
        this.strictService = strictService;
    }

    @Override
    public Flow validate(Invocation<SENDER> invocation, MetaHolder metaHolder) {
        MissingPermissions missingPermissions = MissingPermissions.check(strictService.getPermissionStrictHandler(), invocation.platformSender(), metaHolder);

        if (missingPermissions.isMissing()) {
            return Flow.terminateFlow(missingPermissions);
        }

        return Flow.continueFlow();
    }

}
