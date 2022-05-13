package dev.rollczi.litecommands.command.section;

import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.sugesstion.Suggester;
import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.sugesstion.SuggestResult;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public interface CommandSection extends Suggester {

    String getName();

    Set<String> getAliases();

    boolean isSimilar(String name);

    ExecuteResult execute(LiteInvocation invocation);

    SuggestResult suggestion(LiteInvocation invocation);

    FindResult find(LiteInvocation invocation, int route, FindResult lastResult);

    /**
     * Command section
     */

    void childSection(CommandSection commandSection);

    void mergeSection(CommandSection section);

    List<CommandSection> childrenSection();

    /**
     * Argument executor
     */

    void executor(ArgumentExecutor argumentExecutor);

    List<ArgumentExecutor> executors();

    /**
     * Permissions
     */

    void permission(String... permissions);

    void permission(Collection<String> permissions);

    Collection<String> permissions();

    /**
     * Permissions exclude
     */

    void excludePermission(String... permissions);

    void excludePermission(Collection<String> permissions);

    Collection<String> excludePermissions();

    /**
     * Validator
     */

    void amountValidator(AmountValidator validator);

    void applyOnValidator(Function<AmountValidator, AmountValidator> edit);

    AmountValidator amountValidator();

    /**
     * Apply settings like permissions, excluded permissions and amount validator from another command section.
     *
     * @param other command section from which the settings are extracted.
     */

    default void applySettings(CommandSection other) {
        this.permission(other.permissions());
        this.excludePermission(other.excludePermissions());
        this.amountValidator(other.amountValidator());
    }

}
