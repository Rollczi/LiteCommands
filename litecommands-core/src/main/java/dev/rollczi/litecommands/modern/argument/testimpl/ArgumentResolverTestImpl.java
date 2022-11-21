package dev.rollczi.litecommands.modern.argument.testimpl;

import dev.rollczi.litecommands.modern.argument.annotation.AnnotationArgumentContextBox;
import dev.rollczi.litecommands.modern.argument.annotation.Arg;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResolver;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResultCollector;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResultPreparedCollector;
import dev.rollczi.litecommands.modern.argument.invocation.SuccessfulResult;

public class ArgumentResolverTestImpl implements ArgumentResolver<Void, Arg, String, AnnotationArgumentContextBox<Arg, String>> {

    @Override
    public ArgumentResultCollector<Void> resolve(AnnotationArgumentContextBox<Arg, String> context, ArgumentResultPreparedCollector<Void, String> collector) {
        return collector.collect(1, (invocation, arguments) -> {
            String argument = arguments.get(0);
            Arg annotation = context.getAnnotation();

            if (argument.equals("test")) {
                return ArgumentResult.success(SuccessfulResult.of("test", 1));
            } else {
                return ArgumentResult.failure();
            }
        });
    }

}

