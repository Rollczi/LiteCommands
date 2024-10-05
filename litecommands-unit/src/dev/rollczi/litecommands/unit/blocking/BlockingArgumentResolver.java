package dev.rollczi.litecommands.unit.blocking;

import dev.rollczi.litecommands.annotations.meta.MetaAnnotationKeys;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.lang.reflect.Parameter;

public class BlockingArgumentResolver<SENDER> extends ArgumentResolver<SENDER, BlockingArgument> {

    @Override
    protected ParseResult<BlockingArgument> parse(Invocation<SENDER> invocation, Argument<BlockingArgument> context, String argument) {
        return ParseResult.async(() -> {
            try {
                Parameter parameter = context.meta().get(MetaAnnotationKeys.SOURCE_PARAMETER);

                int blocking = 100;
                Blocking annotation = parameter.getAnnotation(Blocking.class);
                if (annotation != null) {
                    blocking = annotation.value();
                }

                Thread.sleep(blocking);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }

            return ParseResult.success(new BlockingArgument(argument));
        });
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<BlockingArgument> argument, SuggestionContext context) {
        Parameter parameter = argument.meta().get(MetaAnnotationKeys.SOURCE_PARAMETER);

        int blocking = 100;
        Blocking annotation = parameter.getAnnotation(Blocking.class);
        if (annotation != null) {
            blocking = annotation.value();
        }

        return SuggestionResult.of("blocking-" + blocking);
    }
}
