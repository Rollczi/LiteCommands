package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.command.meta.Meta;
import dev.rollczi.litecommands.command.meta.MetaKey;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

class LiteMeta implements Meta {

    private final Map<MetaKey<?>, Object> meta = new HashMap<>();
    private final Set<String> permissions = new HashSet<>();
    private final Set<String> excludedPermissions = new HashSet<>();
    private AmountValidator amountValidator = AmountValidator.NONE;

    @Override
    public void permission(String... permissions) {
        this.permissions.addAll(Arrays.asList(permissions));
    }

    @Override
    public void permission(Collection<String> permissions) {
        this.permissions.addAll(permissions);
    }

    @Override
    public Collection<String> permissions() {
        return Collections.unmodifiableSet(permissions);
    }

    @Override
    public void excludePermission(String... permissions) {
        this.excludedPermissions.addAll(Arrays.asList(permissions));
    }

    @Override
    public void excludePermission(Collection<String> permissions) {
        this.excludedPermissions.addAll(permissions);
    }

    @Override
    public Collection<String> excludePermissions() {
        return Collections.unmodifiableSet(excludedPermissions);
    }

    @Override
    public void amountValidator(AmountValidator validator) {
        this.amountValidator = validator;
    }

    @Override
    public void applyOnValidator(Function<AmountValidator, AmountValidator> edit) {
        this.amountValidator = edit.apply(this.amountValidator);
    }

    @Override
    public AmountValidator amountValidator() {
        return this.amountValidator;
    }

    @Override
    public <T> void set(MetaKey<T> key, T value) {
        this.meta.put(key, value);
    }

    @Override
    public <T> T get(MetaKey<T> key) {
        return key.getType().cast(this.meta.get(key));
    }

    @Override
    public Map<MetaKey<?>, Object> getMeta() {
        return Collections.unmodifiableMap(this.meta);
    }

    @Override
    public void apply(Meta meta) {
        this.amountValidator = meta.amountValidator();
        this.permissions.addAll(meta.permissions());
        this.excludedPermissions.addAll(meta.excludePermissions());
        this.meta.putAll(meta.getMeta());
    }

}
