package dev.rollczi.litecommands.meta;

import dev.rollczi.litecommands.command.amount.AmountValidator;

import java.util.Collection;
import java.util.function.Function;

public interface CommandMeta extends Meta {

    MetaKey<String> USAGE_MESSAGE = MetaKey.of("USAGE_MESSAGE", String.class);

    // Permission

    void addPermission(String... permissions);

    void addPermission(Collection<String> permissions);

    void removePermission(String... permissions);

    void removePermission(Collection<String> permissions);

    void clearPermissions();

    Collection<String> getPermissions();

    // Excluded Permission

    void addExcludedPermission(String... permissions);

    void addExcludedPermission(Collection<String> permissions);

    void removeExcludedPermission(String... permissions);

    void removeExcludedPermission(Collection<String> permissions);

    void clearExcludedPermissions();

    Collection<String> getExcludedPermissions();

    // Amount Validator

    void applyAmountValidator(Function<AmountValidator, AmountValidator> edit);

    AmountValidator getAmountValidator();

    void setAmountValidator(AmountValidator validator);

    // Meta

    void applyCommandMeta(CommandMeta meta);

}
