package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaKey;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

class LiteCommandMeta implements CommandMeta {

    private final Map<MetaKey<?>, Object> meta = new HashMap<>();
    private final Set<String> permissions = new HashSet<>();
    private final Set<String> excludedPermissions = new HashSet<>();
    private AmountValidator amountValidator = AmountValidator.NONE;

    @Override
    public void addPermission(String... permissions) {
        this.permissions.addAll(Arrays.asList(permissions));
    }

    @Override
    public void addPermission(Collection<String> permissions) {
        this.permissions.addAll(permissions);
    }

    @Override
    public void removePermission(String... permissions) {
        Arrays.asList(permissions).forEach(this.permissions::remove);
    }

    @Override
    public void removePermission(Collection<String> permissions) {
        this.permissions.removeAll(permissions);
    }

    @Override
    public void clearPermissions() {
        this.permissions.clear();
    }

    @Override
    public Collection<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    @Override
    public void addExcludedPermission(String... permissions) {
        this.excludedPermissions.addAll(Arrays.asList(permissions));
    }

    @Override
    public void addExcludedPermission(Collection<String> permissions) {
        this.excludedPermissions.addAll(permissions);
    }

    @Override
    public void removeExcludedPermission(String... permissions) {
        Arrays.asList(permissions).forEach(this.excludedPermissions::remove);
    }

    @Override
    public void removeExcludedPermission(Collection<String> permissions) {
        this.excludedPermissions.removeAll(permissions);
    }

    @Override
    public void clearExcludedPermissions() {
        this.excludedPermissions.clear();
    }

    @Override
    public Collection<String> getExcludedPermissions() {
        return Collections.unmodifiableSet(excludedPermissions);
    }

    @Override
    public void setAmountValidator(AmountValidator validator) {
        this.amountValidator = validator;
    }

    @Override
    public void applyAmountValidator(Function<AmountValidator, AmountValidator> edit) {
        this.amountValidator = edit.apply(this.amountValidator);
    }

    @Override
    public AmountValidator getAmountValidator() {
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
        this.meta.putAll(meta.getMeta());
    }

    @Override
    public void applyCommandMeta(CommandMeta meta) {
        this.amountValidator = meta.getAmountValidator();
        this.permissions.addAll(meta.getPermissions());
        this.excludedPermissions.addAll(meta.getExcludedPermissions());
        this.apply((Meta) meta);
    }

}
