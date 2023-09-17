package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.Command;
import dev.rollczi.litecommands.command.executor.Execute;
import dev.rollczi.litecommands.context.Context;
import dev.rollczi.litecommands.flag.Flag;
import dev.rollczi.litecommands.join.Join;
import dev.rollczi.litecommands.permission.Permission;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class LiteCommand<SENDER> {

    private final Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
    private final Map<String, AnnotationHolder<?, ?, ?>> requirements = new HashMap<>();
    private Consumer<LiteContext<SENDER>> executor = command -> {};

    public LiteCommand(String name) {
        this.name(name);
        this.putExecutor();
    }

    public LiteCommand(String name, String... aliases) {
        this.name(name);
        this.aliases(aliases);
        this.putExecutor();
    }

    @Nullable
    @SuppressWarnings("unchecked")
    <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return (A) annotations.get(annotationType);
    }

    @SuppressWarnings("unchecked")
    <A extends Annotation> List<? extends AnnotationHolder<A, ?, ?>> getRequirements(Class<A> annotationType) {
        return requirements.values().stream()
            .filter(holder -> holder.getAnnotation().annotationType().equals(annotationType))
            .map(holder -> (AnnotationHolder<A, ?, ?>) holder)
            .toList();
    }

    Set<String> getRequirements() {
        return requirements.keySet();
    }

    private LiteCommand<SENDER> putAnnotation(Annotation annotation) {
        annotations.put(annotation.annotationType(), annotation);
        return this;
    }

    public LiteCommand<SENDER> name(String name) {
        Command.Mock command = getAnnotation(Command.Mock.class);
        String[] aliases = new String[0];

        if (command != null) {
            aliases = command.aliases();
        }

        return this.putAnnotation(new Command.Mock(name, aliases));
    }

    public LiteCommand<SENDER> aliases(String... aliases) {
        Command.Mock command = getAnnotation(Command.Mock.class);
        String name = "";

        if (command != null) {
            name = command.name();
        }

        return this.putAnnotation(new Command.Mock(name, aliases));
    }

    private LiteCommand<SENDER> putExecutor() {
        return this.putAnnotation(new Execute.Mock("", new String[0]));
    }

    public LiteCommand<SENDER> permission(String... permissions) {
        return this.putAnnotation(new Permission.Mock(permissions));
    }

    public <T> LiteCommand<SENDER> argument(String name, Class<T> type) {
        return this.argument(name, WrapFormat.notWrapped(type));
    }

    public <T> LiteCommand<SENDER> argumentOptional(String name, Class<T> type) {
        return this.argument(name, WrapFormat.of(type, Optional.class));
    }

    public <T, OUT> LiteCommand<SENDER> argument(String name, WrapFormat<T, OUT> wrapFormat) {
        return this.requirement(new Arg.Mock(name), name, wrapFormat);
    }

    public LiteCommand<SENDER> argumentFlag(String name) {
        return this.requirement(new Flag.Mock(name), name, WrapFormat.notWrapped(Boolean.class));
    }

    public LiteCommand<SENDER> argumentJoin(String name) {
        return this.requirement(new Join.Mock(" ", Integer.MAX_VALUE), name, WrapFormat.notWrapped(String.class));
    }

    public LiteCommand<SENDER> argumentJoin(String name, String separator, int limit) {
        return this.requirement(new Join.Mock(separator, limit), name, WrapFormat.notWrapped(String.class));
    }

    public LiteCommand<SENDER> context(String name, Class<?> type) {
        return this.requirement(new Context.Mock(), name, WrapFormat.notWrapped(type));
    }

    public LiteCommand<SENDER> onExecute(Consumer<LiteContext<SENDER>> executor) {
        this.executor = executor;
        return this;
    }

    private <A extends Annotation, T, OUT> LiteCommand<SENDER> requirement(A annotation, String name, WrapFormat<T, OUT> wrapFormat) {
        AnnotationHolder<A, T, OUT> holder = AnnotationHolder.of(annotation, wrapFormat, () -> name);

        this.requirements.put(name, holder);
        return this.putAnnotation(annotation);
    }

    protected void execute(LiteContext<SENDER> liteContext) {
        this.executor.accept(liteContext);
    }

}
