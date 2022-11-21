package dev.rollczi.litecommands.modern.argument.testimpl;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.modern.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.argument.annotation.AnnotationArgumentContextBox;
import dev.rollczi.litecommands.modern.argument.annotation.Arg;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResolverRegistry.IndexKey;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResultCollector;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.platform.LiteSender;

import java.lang.annotation.Annotation;

public class Main {

    public static void main(String[] args) {
        // create service and register resolver
        ArgumentService<Void> argumentService = new ArgumentService<>();
        IndexKey<Arg, String, ArgumentContext<Arg, String>> indexKey = IndexKey.of(Arg.class, String.class, AnnotationArgumentContextBox.class);

        // register resolver
        argumentService.registerResolver(indexKey, new ArgumentResolverTestImpl());


        // create context
        LiteSender liteSender = null;
        Invocation<Void> invocation = new Invocation<>(null, liteSender, "test", "test", new String[]{"test", "test"});

        // create collector and iterate over arguments to resolve
        ArgumentResultCollector<Void> collector = ArgumentResultCollector.create(invocation);
        MethodArgumentContextBoxProvider provider = new MethodArgumentContextBoxProvider(new AnnotationArgumentContextBox<>());

        for (ArgumentContext<Annotation, Object> argStringArgumentContext : provider) {
            collector = resolve(argumentService, argStringArgumentContext, collector);
        }


        for (ArgumentResult<?> result : collector.getResults()) {
            if (result.isSuccessful()) {
                System.out.println(result.getSuccessfulResult().getParsedArgument().get());
            }

            if (result.isFailed()) {
                System.out.println(result.getFailedReason().getReason());
            }
        }
    }

    private static <T, R, CONTEXT extends ArgumentContext<T, R>> ArgumentResultCollector<Void> resolve(ArgumentService<Void> argumentService, CONTEXT contextBox, ArgumentResultCollector<Void> collector) {
        return argumentService.resolve(contextBox, ArgumentKey.DEFAULT, collector);
    }

}
