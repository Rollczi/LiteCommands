package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.KeyAnnotationResolver;
import dev.rollczi.litecommands.annotations.argument.nullable.NullableArgumentProcessor;
import dev.rollczi.litecommands.annotations.async.AsyncAnnotationResolver;
import dev.rollczi.litecommands.annotations.command.CommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.command.RootCommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.cooldown.CooldownAnnotationResolver;
import dev.rollczi.litecommands.annotations.description.DescriptionAnnotationResolver;
import dev.rollczi.litecommands.annotations.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.annotations.flag.FlagAnnotationProcessor;
import dev.rollczi.litecommands.annotations.join.JoinArgumentProcessor;
import dev.rollczi.litecommands.annotations.literal.LiteralArgumentProcessor;
import dev.rollczi.litecommands.annotations.meta.MarkMetaAnnotationResolver;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.optional.OptionalArgArgumentProcessor;
import dev.rollczi.litecommands.annotations.permission.PermissionAnnotationResolver;
import dev.rollczi.litecommands.annotations.permission.PermissionsAnnotationResolver;
import dev.rollczi.litecommands.annotations.priority.PriorityAnnotationResolver;
import dev.rollczi.litecommands.annotations.quoted.QuotedAnnotationProcessor;
import dev.rollczi.litecommands.annotations.requirement.RequirementDefinitionProcessor;
import dev.rollczi.litecommands.annotations.shortcut.ShortcutCommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.validator.ValidateAnnotationResolver;
import dev.rollczi.litecommands.annotations.varargs.VarargsAnyArgumentProcessor;
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
            // universal processor for requirements such as @Arg, @Varargs, @Flag, @Context, @Bind and more
            .register(new RequirementDefinitionProcessor<>())
            // profile processors (they apply profiles to arguments)
            .register(new FlagAnnotationProcessor<>())
            .register(new VarargsArgumentProcessor<>())
            .register(new VarargsAnyArgumentProcessor<>(Arg.class))
            .register(new VarargsAnyArgumentProcessor<>(OptionalArg.class))
            .register(new LiteralArgumentProcessor<>())
            .register(new JoinArgumentProcessor<>())
            .register(new QuotedAnnotationProcessor<>())
            .register(new NullableArgumentProcessor<>())
            .register(new OptionalArgArgumentProcessor<>())
            ;
    }

}
