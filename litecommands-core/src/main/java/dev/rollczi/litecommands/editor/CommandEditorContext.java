package dev.rollczi.litecommands.editor;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.meta.CommandMetaHolder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

public interface CommandEditorContext<SENDER> extends CommandMetaHolder {

    @NotNull
    CommandEditorContext<SENDER> name(String name);

    String name();

    @NotNull
    CommandEditorContext<SENDER> aliases(List<String> aliases);

    @NotNull
    CommandEditorContext<SENDER> aliases(String... aliases);

    boolean isNameOrAlias(String name);

    boolean hasSimilarNames(CommandEditorContext<SENDER> context);

    List<String> aliases();

    List<String> names();

    @NotNull
    CommandEditorContext<SENDER> enable();

    @NotNull
    CommandEditorContext<SENDER> disable();

    @NotNull
    CommandEditorContext<SENDER> editChild(String name, UnaryOperator<CommandEditorContext<SENDER>> operator);

    @NotNull
    CommandEditorContext<SENDER> appendChild(String name, UnaryOperator<CommandEditorContext<SENDER>> operator);

    @NotNull
    CommandEditorContext<SENDER> appendChild(CommandEditorContext<SENDER> context);

    Collection<CommandEditorContext<SENDER>> children();

    @NotNull
    CommandEditorContext<SENDER> appendExecutor(CommandEditorExecutorBuilder<SENDER> executor);

    Collection<CommandEditorExecutorBuilder<SENDER>> executors();

    CommandEditorContext<SENDER> applyMeta(UnaryOperator<CommandMeta> operator);

    CommandMeta getMeta();

    @ApiStatus.Internal
    CommandEditorContext<SENDER> routeName(String name);

    @ApiStatus.Internal
    CommandEditorContext<SENDER> routeAliases(List<String> aliases);

    @ApiStatus.Internal
    CommandEditorContext<SENDER> applyOnRoute(UnaryOperator<CommandEditorContext<SENDER>> apply);

    @ApiStatus.Internal
    void meagre(CommandEditorContext<SENDER> context);

    @ApiStatus.Internal
    boolean isEnabled();

    @ApiStatus.Internal
    boolean buildable();

    @ApiStatus.Internal
    Collection<CommandRoute<SENDER>> build(CommandRoute<SENDER> parent);

    static <SENDER> CommandEditorContext<SENDER> create() {
        return new CommandEditorContextImpl<>();
    }

    static <SENDER> CommandEditorContext<SENDER> createRoot() {
        return new CommandEditorContextRootImpl<>();
    }
}
