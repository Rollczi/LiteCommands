package dev.rollczi.litecommands.command.builder;

import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.scope.Scopeable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandBuilder<SENDER> extends Scopeable, MetaHolder {

    @NotNull
    CommandBuilder<SENDER> name(String name);

    String name();

    @NotNull
    CommandBuilder<SENDER> aliases(List<String> aliases);

    @NotNull
    CommandBuilder<SENDER> aliases(String... aliases);

    boolean isNameOrAlias(String name);

    boolean hasSimilarNames(CommandBuilder<SENDER> context);

    List<String> aliases();

    @Override
    List<String> names();

    @NotNull
    CommandBuilder<SENDER> enable();

    @NotNull
    CommandBuilder<SENDER> enabled(boolean enabled);

    @NotNull
    CommandBuilder<SENDER> disable();

    @NotNull
    CommandBuilder<SENDER> editChild(String name, UnaryOperator<CommandBuilder<SENDER>> operator);

    @NotNull
    CommandBuilder<SENDER> appendChild(String name, UnaryOperator<CommandBuilder<SENDER>> operator);

    @NotNull
    CommandBuilder<SENDER> appendChild(CommandBuilder<SENDER> context);

    Collection<CommandBuilder<SENDER>> children();

    Optional<CommandBuilder<SENDER>> getChild(String test);

    CommandBuilder<SENDER> appendExecutor(CommandExecutorProvider<SENDER> executor);

    Collection<CommandExecutorProvider<SENDER>> executors();

    CommandBuilder<SENDER> applyMeta(UnaryOperator<Meta> operator);

    @Override
    Meta meta();

    @ApiStatus.Internal
    CommandBuilder<SENDER> routeName(String name);

    @ApiStatus.Internal
    CommandBuilder<SENDER> routeAliases(List<String> aliases);

    @ApiStatus.Internal
    CommandBuilder<SENDER> applyOnRoute(UnaryOperator<CommandBuilder<SENDER>> apply);

    @ApiStatus.Internal
    CommandBuilder<SENDER> getRealRoute();

    default boolean hasShortRoute() {
        return false;
    }

    @ApiStatus.Internal
    CommandBuilder<SENDER> shortRoutes(List<String> aliases);

    @ApiStatus.Internal
    void meagre(CommandBuilder<SENDER> context);

    @ApiStatus.Internal
    boolean isEnabled();

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @ApiStatus.Internal
    boolean buildable();

    @ApiStatus.Internal
    @Nullable
    CommandBuilder<SENDER> parent();

    @ApiStatus.Internal
    Collection<CommandRoute<SENDER>> build(CommandRoute<SENDER> parent);

    static <SENDER> CommandBuilder<SENDER> create() {
        return new CommandBuilderImpl<>();
    }

    static <SENDER> CommandBuilder<SENDER> createRoot() {
        return new CommandBuilderRootImpl<>();
    }

}
