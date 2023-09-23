package dev.rollczi.litecommands.annotation.processor;

import dev.rollczi.litecommands.annotation.RequirementProcessor;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.ArgArgumentFactory;
import dev.rollczi.litecommands.async.AsyncAnnotationResolver;
import dev.rollczi.litecommands.command.CommandAnnotationProcessor;
import dev.rollczi.litecommands.command.RootCommandAnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.context.Context;
import dev.rollczi.litecommands.description.DescriptionAnnotationResolver;
import dev.rollczi.litecommands.flag.Flag;
import dev.rollczi.litecommands.flag.FlagArgumentFactory;
import dev.rollczi.litecommands.join.Join;
import dev.rollczi.litecommands.join.JoinArgumentFactory;
import dev.rollczi.litecommands.meta.MarkMetaAnnotationResolver;
import dev.rollczi.litecommands.permission.PermissionAnnotationResolver;
import dev.rollczi.litecommands.permission.PermissionsAnnotationResolver;
import dev.rollczi.litecommands.validator.ValidateAnnotationResolver;

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
            .register(new CommandAnnotationProcessor<>())
            .register(new RootCommandAnnotationProcessor<>())
            .register(new MarkMetaAnnotationResolver<>())
            .register(new DescriptionAnnotationResolver<>())
            .register(new AsyncAnnotationResolver<>())
            .register(new PermissionAnnotationResolver<>())
            .register(new PermissionsAnnotationResolver<>())
            .register(new ValidateAnnotationResolver<>())
            .register(new ExecuteAnnotationResolver<>())
            .register(new RequirementProcessor<>(Flag.class, new FlagArgumentFactory()))
            .register(new RequirementProcessor<>(Arg.class, new ArgArgumentFactory()))
            .register(new RequirementProcessor<>(Join.class, new JoinArgumentFactory()))
            .register(new RequirementProcessor<>(Context.class));
    }

}
