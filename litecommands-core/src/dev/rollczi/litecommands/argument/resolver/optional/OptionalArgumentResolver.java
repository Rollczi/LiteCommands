package dev.rollczi.litecommands.argument.resolver.optional;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.ParserChainAccessor;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolverChained;
import dev.rollczi.litecommands.argument.suggester.SuggesterChainAccessor;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.Optional;

@SuppressWarnings("rawtypes")
public class OptionalArgumentResolver<SENDER> implements MultipleArgumentResolverChained<SENDER, Optional> {

    @Override
    public ParseResult<Optional> parse(Invocation<SENDER> invocation, Argument<Optional> optionalArgument, RawInput input, ParserChainAccessor<SENDER> chainAccessor) {
        TypeToken<Optional> optionalType = optionalArgument.getType();
        TypeToken<?> parameterized = optionalType.getParameterized();

        return parseValue(parameterized, invocation, optionalArgument, input, chainAccessor);
    }

    @SuppressWarnings("unchecked")
    private <E> ParseResult<Optional> parseValue(TypeToken<E> type, Invocation<SENDER> invocation, Argument<Optional> optionalArgument, RawInput input, ParserChainAccessor<SENDER> chainAccessor) {
        Argument<E> argument = Argument.of(optionalArgument.getName(), type);
        ParseResult parseResult = chainAccessor.parse(invocation, argument.child(type), input);

        return parseResult
            .map(value -> Optional.of(value))
            .mapFailure(failure -> failure == InvalidUsage.Cause.MISSING_ARGUMENT
                ? ParseResult.success(Optional.empty())
                : ParseResult.failure(failure)
            );
    }

    @Override
    public Range getRange(Argument<Optional> optionalArgument) {
        return Range.range(0, 1);
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Optional> argument, SuggestionContext context, SuggesterChainAccessor<SENDER> chainAccessor) {
        TypeToken<Optional> optionalType = argument.getType();
        TypeToken<?> parameterized = optionalType.getParameterized();

        return chainAccessor.suggest(invocation, Argument.of(argument.getName(), parameterized), context);
    }

}
