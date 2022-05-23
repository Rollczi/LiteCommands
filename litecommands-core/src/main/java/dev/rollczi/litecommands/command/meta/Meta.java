package dev.rollczi.litecommands.command.meta;

import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.command.permission.LitePermissions;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public interface Meta {

    MetaKey<String> USAGE_MESSAGE = MetaKey.of("USAGE_MESSAGE", String.class);
    MetaKey<LitePermissions> MISSING_PERMISSIONS = MetaKey.of("MISSING_PERMISSIONS", LitePermissions.class);

    void permission(String... permissions);

    void permission(Collection<String> permissions);

    Collection<String> permissions();

    void excludePermission(String... permissions);

    void excludePermission(Collection<String> permissions);

    Collection<String> excludePermissions();

    void amountValidator(AmountValidator validator);

    void applyOnValidator(Function<AmountValidator, AmountValidator> edit);

    AmountValidator amountValidator();

    <T> void set(MetaKey<T> key, T value);

    <T> T get(MetaKey<T> key);

    Map<MetaKey<?>, Object> getMeta();

    void apply(Meta meta);

}
