package dev.rollczi.litecommands.scope;

import dev.rollczi.litecommands.valid.ValidationInfo;
import dev.rollczi.litecommands.valid.AmountValidator;
import panda.std.Option;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class ScopeMetaData {

    private final String name;
    private final int priority;
    private final Set<String> aliases = new HashSet<>();
    private final Map<ValidationInfo, String> messages = new EnumMap<>(ValidationInfo.class);
    private final AmountValidator argCountValidator;
    private final Set<String> permissions = new HashSet<>();
    private final Set<String> permissionsExclude = new HashSet<>();

    private ScopeMetaData(String name, int priority, Map<ValidationInfo, String> messages, AmountValidator argCountValidator, Set<String> permissions, Set<String> permissionsExclude, Set<String> aliases) {
        this.name = name;
        this.priority = priority;
        this.aliases.addAll(aliases);
        this.messages.putAll(messages);
        this.argCountValidator = argCountValidator;
        this.permissions.addAll(permissions);
        this.permissionsExclude.addAll(permissionsExclude);
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public Set<String> getAliases() {
        return Collections.unmodifiableSet(aliases);
    }

    public Option<String> getMessage(ValidationInfo info) {
        return Option.of(messages.get(info));
    }

    public AmountValidator getArgsValidator() {
        return argCountValidator;
    }

    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    public Set<String> getPermissionsExclude() {
        return Collections.unmodifiableSet(permissionsExclude);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ScopeMetaData fromName(String name) {
        return builder()
                .name(name)
                .build();
    }

    public static ScopeMetaData fromName(String name, Collection<String> aliases) {
        return builder()
                .name(name)
                .aliases(aliases)
                .build();
    }

    public static class Builder {

        private String name;
        private int priority = 0;
        private final Set<String> aliases = new HashSet<>();
        private final Map<ValidationInfo, String> messages = new EnumMap<>(ValidationInfo.class);
        private AmountValidator amountValidator = AmountValidator.NONE;
        private final Set<String> permissions = new HashSet<>();
        private final Set<String> permissionsExclude = new HashSet<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder aliases(Collection<String> aliases) {
            this.aliases.addAll(aliases);
            return this;
        }

        public Builder aliases(String... aliases) {
            this.aliases.addAll(Arrays.asList(aliases));
            return this;
        }

        public Builder message(ValidationInfo info, String message) {
            this.messages.put(info, message);
            return this;
        }

        public Builder amountValidator(Function<AmountValidator, AmountValidator> validator) {
            this.amountValidator = validator.apply(this.amountValidator);
            return this;
        }

        public Builder permissions(Set<String> permissions) {
            this.permissions.addAll(permissions);
            return this;
        }

        public Builder permissions(String... permissions) {
            this.permissions.addAll(Arrays.asList(permissions));
            return this;
        }

        public Builder permissionsExclude(Set<String> permissionsExclude) {
            this.permissionsExclude.addAll(permissionsExclude);
            return this;
        }

        public Builder permissionsExclude(String... permissionsExclude) {
            this.permissionsExclude.addAll(Arrays.asList(permissionsExclude));
            return this;
        }

        public ScopeMetaData build() {
            if (name == null) {
                throw new IllegalStateException("name is null");
            }

            return new ScopeMetaData(name, priority, messages, amountValidator, permissions, permissionsExclude, aliases);
        }

    }

}
