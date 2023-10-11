package dev.rollczi.litecommands.command.builder;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

abstract class CommandBuilderChildrenBase<SENDER> implements CommandBuilder<SENDER> {

    protected final Map<String, CommandBuilder<SENDER>> children = new HashMap<>();

    @Override
    public @NotNull CommandBuilder<SENDER> editChild(String name, UnaryOperator<CommandBuilder<SENDER>> operator) {
        Optional<CommandBuilder<SENDER>> builderOptional = this.getChild(name);

        if (!builderOptional.isPresent()) {
            throw new IllegalArgumentException("Child " + name + " not found");
        }

        CommandBuilder<SENDER> edited = operator.apply(builderOptional.get());

        this.children.put(name, edited);
        return this;
    }

    @Override
    public @NotNull CommandBuilder<SENDER> appendChild(String name, UnaryOperator<CommandBuilder<SENDER>> operator) {
        CommandBuilder<SENDER> child = new CommandBuilderImpl<SENDER>()
            .name(name);

        CommandBuilder<SENDER> edited = operator.apply(child);

        return this.appendChild(edited);
    }

    @Override
    public @NotNull CommandBuilder<SENDER> appendChild(CommandBuilder<SENDER> builder) {
        Optional<CommandBuilder<SENDER>> optionalDuplicate = this.getChild(builder.name());

        if (optionalDuplicate.isPresent()) {
            CommandBuilder<SENDER> duplicate = optionalDuplicate.get();

            duplicate.meagre(builder);
            return this;
        }

        this.children.put(builder.name(), builder);
        return this;
    }

    @Override
    public Collection<CommandBuilder<SENDER>> children() {
        return Collections.unmodifiableCollection(children.values());
    }

    @Override
    public Optional<CommandBuilder<SENDER>> getChild(String test) {
        CommandBuilder<SENDER> context = children.get(test);

        if (context != null) {
            return Optional.of(context);
        }

        for (CommandBuilder<SENDER> child : children.values()) {
            if (child.isNameOrAlias(test)) {
                return Optional.of(child);
            }
        }

        return Optional.empty();
    }

}
