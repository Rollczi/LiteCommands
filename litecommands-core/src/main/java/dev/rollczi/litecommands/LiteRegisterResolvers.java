package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.component.LiteMultiSection;
import dev.rollczi.litecommands.component.LiteSection;
import dev.rollczi.litecommands.component.ScopeMetaData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LiteRegisterResolvers {

    private final Map<String, LiteComponent> resolvers = new HashMap<>();

    public void register(LiteSection section) {
        String name = section.getScope().getName();
        Set<String> aliases = section.getScope().getAliases();

        if (!resolvers.containsKey(name)) {
            resolvers.put(name, section);
            return;
        }

        LiteComponent removed = resolvers.remove(name);
        ScopeMetaData.Builder scope = ScopeMetaData.builder()
                .name(name)
                .aliases(aliases);
        LiteMultiSection.Builder multi = LiteMultiSection.builder()
                .name(name)
                .sections(section);

        if (removed instanceof LiteMultiSection) {
            LiteMultiSection oldMultiSection = (LiteMultiSection) removed;

            scope.aliases(oldMultiSection.getScope().getAliases());
            multi.sections(oldMultiSection.getSections());
        } else if (removed instanceof LiteSection) {
            LiteSection oldSection = (LiteSection) removed;

            scope.aliases(oldSection.getScope().getAliases());
            multi.sections(oldSection);
        }

        multi.scopeInformation(scope.build());
        resolvers.put(name, multi.build());
    }

    public Map<String, LiteComponent> getResolvers() {
        return Collections.unmodifiableMap(resolvers);
    }
}
