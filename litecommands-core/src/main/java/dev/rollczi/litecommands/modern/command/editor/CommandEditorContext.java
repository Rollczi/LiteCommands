package dev.rollczi.litecommands.modern.command.editor;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandRoute;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

public interface CommandEditorContext<SENDER> {

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
    CommandEditorContext<SENDER> appendExecutor(CommandExecutor<SENDER> executor);

    Collection<CommandExecutor<SENDER>> executors();

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
    CommandRoute<SENDER> build();

    static <SENDER> CommandEditorContext<SENDER> empty() {
        return new CommandEditorContextImpl<>();
    }

}
