package dev.rollczi.litecommands.meta;

import dev.rollczi.litecommands.command.amount.AmountValidator;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public interface CommandMeta extends Meta {

    MetaKey<Boolean> ASYNCHRONOUS = MetaKey.of("asynchronous", Boolean.class, false);

    @Override
    <T> CommandMeta set(MetaKey<T> key, T value);

    @Override
    <T> T get(MetaKey<T> key);

    @Override
    Map<MetaKey<?>, Object> getMeta();

    @Override
    CommandMeta apply(Meta meta);

    // Permission

    CommandMeta addPermission(String... permissions);

    CommandMeta addPermission(Collection<String> permissions);

    CommandMeta removePermission(String... permissions);

    CommandMeta removePermission(Collection<String> permissions);

    CommandMeta clearPermissions();

    Collection<String> getPermissions();

    // Excluded Permission

    CommandMeta addExcludedPermission(String... permissions);

    CommandMeta addExcludedPermission(Collection<String> permissions);

    CommandMeta removeExcludedPermission(String... permissions);

    CommandMeta removeExcludedPermission(Collection<String> permissions);

    CommandMeta clearExcludedPermissions();

    Collection<String> getExcludedPermissions();

    // Amount Validator

    CommandMeta applyAmountValidator(Function<AmountValidator, AmountValidator> edit);

    AmountValidator getAmountValidator();

    CommandMeta setAmountValidator(AmountValidator validator);

    // Meta

    CommandMeta applyCommandMeta(CommandMeta meta);

    static CommandMeta create() {
        return new CommandMetaImpl();
    }

}
