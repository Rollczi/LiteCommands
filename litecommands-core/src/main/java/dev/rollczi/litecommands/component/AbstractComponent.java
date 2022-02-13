package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import panda.std.stream.PandaStream;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<String> getMissingPermission(ContextOfResolving context, boolean execution) {
        if (execution && !(this instanceof LiteExecution)) {
            return Collections.emptyList();
        }

        LiteSender sender = context.getInvocation().sender();
        ContextOfResolving currentContext = context.resolverNestingTracing(this);
        Set<String> permissions = new HashSet<>();

        PandaStream.of(currentContext.getTracesOfResolvers()).map(LiteComponent::getScope).forEach(scope -> {
            permissions.addAll(scope.getPermissions());
            permissions.removeAll(scope.getPermissionsExclude());
        });

        return permissions.stream()
                .filter(permission -> !sender.hasPermission(permission))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasValidArgs(ContextOfResolving context) {
        return this.getScope().getArgsValidator().valid(context.getCurrentArgsCount(this));
    }

}
