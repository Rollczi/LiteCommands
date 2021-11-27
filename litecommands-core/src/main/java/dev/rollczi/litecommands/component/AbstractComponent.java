package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.LiteSender;
import panda.std.stream.PandaStream;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractComponent implements LiteComponent {

    protected final ScopeMetaData scope;

    AbstractComponent(ScopeMetaData scope) {
        this.scope = scope;
    }

    @Override
    public ScopeMetaData getScope() {
        return scope;
    }

    @Override
    public boolean hasPermission(ContextOfResolving context) {
        if (!(this instanceof LiteExecution)) {
            return true;
        }

        LiteSender sender = context.getInvocation().sender();
        ContextOfResolving currentContext = context.resolverNestingTracing(this);
        Set<String> permissions = new HashSet<>();

        PandaStream.of(currentContext.getTracesOfResolvers()).map(LiteComponent::getScope).forEach(scope -> {
            permissions.addAll(scope.getPermissions());
            permissions.removeAll(scope.getPermissionsExclude());
        });

        for (String permission : permissions) {
            if (!sender.hasPermission(permission)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean hasValidArgs(ContextOfResolving context) {
        return this.getScope().getArgsValidator().valid(context.getCurrentArgsCount(this));
    }

}
