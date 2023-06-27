package dev.rollczi.litecommands.annotations.argument.join;

import dev.rollczi.litecommands.annotations.argument.AnnotationArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;

import java.util.ArrayList;
import java.util.List;

class JoinArgumentResolver<SENDER> extends AnnotationArgumentResolver<SENDER, String, JoinArgument<String>> {

    public JoinArgumentResolver() {
        super(JoinArgument.class);
    }

    @Override
    public ParseResult<String> parseTyped(Invocation<SENDER> invocation, JoinArgument<String> argument, RawInput rawInput) {
        Join join = argument.getAnnotation();

        int limit = join.limit();
        List<String> values = new ArrayList<>();

        if (!rawInput.hasNext()) {
            return ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
        }

        while (limit > 0 && rawInput.hasNext()) {
            values.add(rawInput.next());
        }

        return ParseResult.success(String.join(join.separator(), values));
    }

    @Override
    public Range getTypedRange(JoinArgument<String> argument) {
        return Range.range(1, argument.getAnnotation().limit());
    }

    @Override
    public SuggestionResult suggestTyped(Invocation<SENDER> invocation, JoinArgument<String> argument, SuggestionContext context) {
        return SuggestionResult.of("Simple text...");
    }


}
