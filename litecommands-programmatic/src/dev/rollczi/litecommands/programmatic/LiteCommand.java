package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.LiteContext;
import dev.rollczi.litecommands.flag.FlagProfile;
import dev.rollczi.litecommands.join.JoinProfile;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.quoted.QuotedProfile;
import dev.rollczi.litecommands.bind.BindRequirement;
import dev.rollczi.litecommands.context.ContextRequirement;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.strict.StrictMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.ApiStatus;

public class LiteCommand<SENDER> {

    protected final String name;
    protected final List<String> aliases;
    protected final Meta meta = Meta.create();
    protected final Meta executorMeta = Meta.create();

    protected Function<LiteContext<SENDER>, Object> executor = liteContext -> null;
    protected boolean withExecutor = true;
    protected final List<Argument<?>> arguments = new ArrayList<>();
    protected final List<ContextRequirement<?>> contextRequirements = new ArrayList<>();
    protected final List<BindRequirement<?>> bindRequirements = new ArrayList<>();

    protected final List<LiteCommand<SENDER>> subCommands = new ArrayList<>();

    public LiteCommand(String name, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public LiteCommand(String name) {
        this(name, Collections.emptyList());
    }

    public LiteCommand(String name, String... aliases) {
        this(name, Arrays.asList(aliases));
    }

    public LiteCommand<SENDER> argument(String name, Class<?> type) {
        this.arguments.add(Argument.of(name, type));
        return this;
    }

    public LiteCommand<SENDER> argument(Argument<?> argument) {
        this.arguments.add(argument);
        return this;
    }

    public LiteCommand<SENDER> argumentQuoted(String name) {
        return this.argument(Argument.profiled(name, String.class, new QuotedProfile()));
    }

    public <T> LiteCommand<SENDER> argumentOptional(String name, Class<T> type) {
        this.arguments.add(Argument.of(name, TypeToken.ofParameterized(Optional.class, type)));
        return this;
    }

    public <T> LiteCommand<SENDER> argumentNullable(String name, Class<T> type) {
        this.arguments.add(Argument.of(name, TypeToken.ofParameterized(Optional.class, type)));
        return this;
    }

    public LiteCommand<SENDER> argumentFlag(String name) {
        this.arguments.add(Argument.profiled(name, Boolean.class, new FlagProfile(name)));
        return this;
    }

    public LiteCommand<SENDER> argumentJoin(String name) {
        this.arguments.add(Argument.profiled(name, String.class, new JoinProfile()));
        return this;
    }

    public LiteCommand<SENDER> argumentJoin(String name, String separator, int limit) {
        this.arguments.add(Argument.profiled(name, String.class, new JoinProfile(separator, limit)));
        return this;
    }

    public LiteCommand<SENDER> context(String name, Class<?> type) {
        this.contextRequirements.add(ContextRequirement.of(() -> name, type));
        return this;
    }

    public LiteCommand<SENDER> bind(String name, Class<?> type) {
        this.bindRequirements.add(BindRequirement.of(() -> name, type));
        return this;
    }

    public LiteCommand<SENDER> permissions(String... permissions) {
        this.meta.listEditor(Meta.PERMISSIONS).addAll(permissions).apply();
        return this;
    }

    public LiteCommand<SENDER> async() {
        return this.meta(Meta.POLL_TYPE, SchedulerPoll.ASYNCHRONOUS);
    }

    public <T> LiteCommand<SENDER> meta(MetaKey<T> key, T value) {
        this.meta.put(key, value);
        return this;
    }

    /**
     * @deprecated Use {@link #execute(Consumer)} instead
     */
    @Deprecated
    public final LiteCommand<SENDER> onExecute(Consumer<LiteContext<SENDER>> executor) {
        return this.execute(executor);
    }

    public final LiteCommand<SENDER> execute(Consumer<LiteContext<SENDER>> executor) {
        this.executor = liteContext -> {
            executor.accept(liteContext);
            return null;
        };
        return this;
    }

    @ApiStatus.Experimental
    public final LiteCommand<SENDER> executeReturn(Function<LiteContext<SENDER>, Object> executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Disables the executor for this command. Useful when you want to use subcommands only.
     */
    @ApiStatus.Experimental
    public LiteCommand<SENDER> withoutExecutor() {
        this.executor = liteContext -> null;
        this.withExecutor = false;
        return this;
    }

    @ApiStatus.Experimental
    public LiteCommand<SENDER> strict(StrictMode strict) {
        this.meta.put(Meta.STRICT_MODE, strict);
        return this;
    }

    @ApiStatus.Experimental
    public LiteCommand<SENDER> strictExecutor(StrictMode strict) {
        this.executorMeta.put(Meta.STRICT_MODE, strict);
        return this;
    }

    protected void execute(LiteContext<SENDER> context) {
        Object object = this.executor.apply(context);
        context.returnResult(object);
    }

    @SafeVarargs
    @Deprecated
    public final LiteCommand<SENDER> subCommands(LiteCommand<SENDER>... subCommands) {
        this.subCommands.addAll(Arrays.asList(subCommands));
        return this;
    }

    @SafeVarargs
    public final LiteCommand<SENDER> subcommands(LiteCommand<SENDER>... subCommands) {
        this.subCommands.addAll(Arrays.asList(subCommands));
        return this;
    }

    public final LiteCommand<SENDER> subcommand(String name, UnaryOperator<LiteCommand<SENDER>> operator) {
        this.subCommands.add(operator.apply(new LiteCommand<>(name)));
        return this;
    }

    CommandBuilder<SENDER> toRoute() {
        CommandBuilder<SENDER> builder = CommandBuilder.<SENDER>create()
            .routeName(name)
            .routeAliases(aliases)
            .applyMeta(meta -> meta.putAll(this.meta))
            .applyMeta(meta -> meta.listEditor(Meta.COMMAND_ORIGIN_TYPE).add(this.getClass()).apply());

        if (withExecutor) {
            builder.getRealRoute().appendExecutor(root -> CommandExecutor.builder(root)
                .executor(liteContext -> execute(liteContext))
                .arguments(arguments)
                .contextRequirements(contextRequirements)
                .bindRequirements(bindRequirements)
                .apply(commandExecutor -> commandExecutor.meta().putAll(this.executorMeta))
                .build()
            );
        }

        for (LiteCommand<SENDER> subCommand : subCommands) {
            builder.appendChild(subCommand.toRoute());
        }

        return builder;
    }

}
