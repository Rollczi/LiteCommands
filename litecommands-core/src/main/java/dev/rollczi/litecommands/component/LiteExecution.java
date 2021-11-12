package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.LiteSender;
import dev.rollczi.litecommands.inject.InjectContext;
import dev.rollczi.litecommands.valid.Valid;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import dev.rollczi.litecommands.valid.ValidationInfo;
import org.slf4j.Logger;
import panda.std.stream.PandaStream;

import java.util.HashSet;
import java.util.Set;

public final class LiteExecution extends AbstractComponent {

    private final Logger logger;
    private final MethodExecutor executor;

    LiteExecution(Logger logger, ScopeMetaData scopeMetaData, MethodExecutor executor) {
        super(scopeMetaData);
        this.logger = logger;
        this.executor = executor;
    }

    @Override
    public void resolve(Data data) {
        LiteInvocation invocation = data.getInvocation();
        LiteSender sender = invocation.sender();

        Set<String> permissions = new HashSet<>(this.scope.getPermissions());

        PandaStream.of(data.getTracedResolvers()).map(LiteComponent::getScope).concat(this.scope).forEach(scope -> {
            permissions.addAll(scope.getPermissions());
            permissions.removeAll(scope.getPermissionsExclude());
        });

        for (String permission : permissions) {
            Valid.whenWithContext(!sender.hasPermission(permission), ValidationInfo.NO_PERMISSION, data, this);
        }

        Valid.whenWithContext(!scope.getArgsValidator().valid(data.getCurrentArgsCount(this)), ValidationInfo.INCORRECT_USE, data, this);

        executor.execute(new InjectContext(data, this)).onError(error -> {
            if (error.getSecond() instanceof ValidationCommandException exception) {
                Valid.whenWithContext(exception.getMessage() == null, exception.getValidationInfo(), data, this);

                throw exception;
            }

            logger.error(error.getFirst(), error.getSecond());
        });
    }

}
