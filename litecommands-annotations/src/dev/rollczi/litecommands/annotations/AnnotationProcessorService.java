package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.argument.ArgArgumentProcessor;
import dev.rollczi.litecommands.annotations.argument.KeyAnnotationResolver;
import dev.rollczi.litecommands.annotations.argument.collector.ArgCollectionArgumentProcessor;
import dev.rollczi.litecommands.annotations.async.AsyncAnnotationResolver;
import dev.rollczi.litecommands.annotations.bind.BindRequirementProcessor;
import dev.rollczi.litecommands.annotations.command.CommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.command.RootCommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.context.ContextRequirementProcessor;
import dev.rollczi.litecommands.annotations.cooldown.CooldownAnnotationResolver;
import dev.rollczi.litecommands.annotations.description.DescriptionAnnotationResolver;
import dev.rollczi.litecommands.annotations.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.annotations.flag.FlagArgumentProcessor;
import dev.rollczi.litecommands.annotations.join.JoinArgumentProcessor;
import dev.rollczi.litecommands.annotations.meta.MarkMetaAnnotationResolver;
import dev.rollczi.litecommands.annotations.optional.OptionalArgArgumentProcessor;
import dev.rollczi.litecommands.annotations.permission.PermissionAnnotationResolver;
import dev.rollczi.litecommands.annotations.permission.PermissionsAnnotationResolver;
import dev.rollczi.litecommands.annotations.priority.PriorityAnnotationResolver;
import dev.rollczi.litecommands.annotations.quoted.QuotedAnnotationProcessor;
import dev.rollczi.litecommands.annotations.shortcut.ShortcutCommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.validator.ValidateAnnotationResolver;
import dev.rollczi.litecommands.annotations.varargs.VarargsArgumentProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import java.util.ArrayList;
import java.util.List;

public class AnnotationProcessorService<SENDER> {

    private final List<AnnotationProcessor<SENDER>> annotationProcessors = new ArrayList<>();

    public <A extends AnnotationProcessor<SENDER>> AnnotationProcessorService<SENDER> register(A processor) {
        annotationProcessors.add(processor);
        return this;
    }

    public CommandBuilder<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        for (AnnotationProcessor<SENDER> processor : annotationProcessors) {
            invoker = processor.process(invoker);
        }

        return invoker.getResult();
    }

    public static <SENDER> AnnotationProcessorService<SENDER> defaultService() {
        return new AnnotationProcessorService<SENDER>()
            // class processors
            .register(new CommandAnnotationProcessor<>())
            .register(new RootCommandAnnotationProcessor<>())
            // method processors
            .register(new ExecuteAnnotationResolver<>())
            .register(new ShortcutCommandAnnotationProcessor<>())
            // meta holder processors
            .register(new MarkMetaAnnotationResolver<>())
            .register(new DescriptionAnnotationResolver<>())
            .register(new AsyncAnnotationResolver<>())
            .register(new PermissionAnnotationResolver<>())
            .register(new PermissionsAnnotationResolver<>())
            .register(new PriorityAnnotationResolver<>())
            .register(new ValidateAnnotationResolver<>())
            .register(new CooldownAnnotationResolver<>())
            // argument meta processors
            .register(new KeyAnnotationResolver<>())
            .register(new QuotedAnnotationProcessor<>())
            // argument processors
            .register(new FlagArgumentProcessor<>())
            .register(new VarargsArgumentProcessor<>())
            .register(new ArgCollectionArgumentProcessor<>())
            .register(new ArgArgumentProcessor<>())
            .register(new OptionalArgArgumentProcessor<>())
            .register(new JoinArgumentProcessor<>())
            // other requirements processors
            .register(new ContextRequirementProcessor<>())
            .register(new BindRequirementProcessor<>());
    }

}
