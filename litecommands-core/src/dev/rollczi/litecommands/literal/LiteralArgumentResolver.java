package dev.rollczi.litecommands.literal;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.profile.ProfileNamespaces;
import dev.rollczi.litecommands.argument.profile.ProfiledMultipleArgumentResolver;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.ArrayList;
import java.util.List;

public class LiteralArgumentResolver<SENDER> extends ProfiledMultipleArgumentResolver<SENDER, String, LiteralProfile> {

    public LiteralArgumentResolver() {
        super(ProfileNamespaces.LITERAL);
    }

    @Override
    public ParseResult<String> parse(Invocation<SENDER> invocation, Argument<String> argument, RawInput rawInput, LiteralProfile literalProfile) {
        return parse(new ArrayList<>(), rawInput, literalProfile);
    }

    private ParseResult<String> parse(List<String> before, RawInput rawInput, LiteralProfile literalProfile) {
        int max = literalProfile.getExpectedRange().getMax();

        if (before.size() >= max) {
            return ParseResult.failure(FailedReason.of(InvalidUsage.Cause.INVALID_ARGUMENT));
        }

        if (!rawInput.hasNext()) {
            return ParseResult.failure(FailedReason.of(InvalidUsage.Cause.MISSING_ARGUMENT));
        }

        String partOfLiteral = rawInput.next();
        String fullLiteral = before.isEmpty() ? partOfLiteral : String.join(" ", before) + " " + partOfLiteral;

        if (literalProfile.getLiterals().contains(fullLiteral)) {
            return ParseResult.success(fullLiteral);
        }

        before.add(partOfLiteral);
        return parse(before, rawInput, literalProfile);
    }

    @Override
    public Range getRange(Argument<String> argument, LiteralProfile literalProfile) {
        return literalProfile.getExpectedRange();
    }

    @Override
    protected SuggestionResult suggest(Invocation<SENDER> invocation, Argument<String> argument, SuggestionContext context, LiteralProfile literalProfile) {
        return SuggestionResult.of(literalProfile.getLiterals());
    }

}
