package dev.rollczi.litecommands.modern.command.editor;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.UnaryOperator;

public interface CommandEditorContext {

    @NotNull
    CommandEditorContext name(String name);

    String name();

    @NotNull
    CommandEditorContext aliases(List<String> aliases);

    List<String> aliases();

    List<String> names();

    @NotNull
    CommandEditorContext enable();

    @NotNull
    CommandEditorContext disable();

    @NotNull
    CommandEditorContext editChild(String name, UnaryOperator<CommandEditorContext> operator);

    @NotNull
    CommandEditorContext appendChild(String name, UnaryOperator<CommandEditorContext> operator);

    @NotNull
    CommandEditorContext appendChild(CommandEditorContext context);

    @NotNull
    CommandEditorContext appendExecutor(CommandExecutor executor);

    @ApiStatus.Internal
    CommandEditorContext routeName(String name);

    @ApiStatus.Internal
    CommandEditorContext routeAliases(List<String> aliases);

    @ApiStatus.Internal
    CommandEditorContext applyOnRoute(UnaryOperator<CommandEditorContext> apply);

    @ApiStatus.Internal
    boolean buildable();

    static CommandEditorContext empty() {
        return new CommandEditorContextImpl();
    }

}
