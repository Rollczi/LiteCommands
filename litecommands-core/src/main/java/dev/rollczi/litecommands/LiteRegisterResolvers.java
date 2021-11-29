package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.component.LiteSection;
import dev.rollczi.litecommands.component.ScopeMetaData;
import dev.rollczi.litecommands.component.ScopeUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LiteRegisterResolvers {

    private final Map<String, LiteSection> resolvers = new HashMap<>();

    public void register(LiteSection section) {
        ScopeMetaData scope = section.getScope();
        String name = scope.getName();

        if (!resolvers.containsKey(name)) {
            resolvers.put(name, section);
            return;
        }

        LiteSection removed = resolvers.remove(name);
        ScopeMetaData scopeRemoved = removed.getScope();

        if (scopeRemoved.getPriority() == scope.getPriority()) {
            throw new UnsupportedOperationException(ScopeUtils.annotationFormat(scopeRemoved) + " sections have the same priorities!");
        }

        LiteSection build = LiteSection.builder()
                .resolvers(removed.getResolvers())
                .resolvers(section.getResolvers())
                .scopeInformation(scopeRemoved.getPriority() > scope.getPriority() ? scopeRemoved : scope)
                .build();

        resolvers.put(name, build);
    }

    public Map<String, LiteComponent> getResolvers() {
        return Collections.unmodifiableMap(resolvers);
    }
}
