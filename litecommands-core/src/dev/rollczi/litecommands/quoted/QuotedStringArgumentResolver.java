package dev.rollczi.litecommands.quoted;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.ArrayList;
import java.util.List;

public class QuotedStringArgumentResolver<SENDER> implements MultipleArgumentResolver<SENDER, String> {

    public final static String KEY = "quoted-string";

    private final MessageRegistry<SENDER> messageRegistry;
    private final SuggesterRegistry<SENDER> suggesterRegistry;
    private final String quote;

    public QuotedStringArgumentResolver(MessageRegistry<SENDER> messageRegistry, SuggesterRegistry<SENDER> suggesterRegistry, String quote) {
        this.messageRegistry = messageRegistry;
        this.suggesterRegistry = suggesterRegistry;
        this.quote = quote;
    }

    public QuotedStringArgumentResolver(MessageRegistry<SENDER> messageRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        this(messageRegistry, suggesterRegistry, "\"");
    }

    @Override
    public ParseResult<String> parse(Invocation<SENDER> invocation, Argument<String> argument, RawInput rawInput) {
        if (!rawInput.hasNext()) {
            throw new IllegalArgumentException("To parse argument, you need to provide at least one argument.");
        }

        String first = rawInput.next();

        if (!first.startsWith(quote)) {
            QuotedError error = new QuotedError(first, QuotedError.Cause.FIRST_QUOTE);
            return ParseResult.failure(this.messageRegistry.getInvoked(LiteMessages.QUOTED_STRING_INVALID_FORMAT, invocation, error));
        }

        if (first.endsWith(quote)) {
            return ParseResult.success(first.substring(1, first.length() - 1));
        }

        StringBuilder builder = new StringBuilder(first.substring(1));

        while (rawInput.hasNext()) {
            String next = rawInput.next();

            builder.append(" ");

            if (!next.endsWith(quote)) {
                builder.append(next);
                continue;
            }

            builder.append(next, 0, next.length() - 1);
            return ParseResult.success(builder.toString());
        }

        QuotedError error = new QuotedError(builder.toString(), QuotedError.Cause.LAST_QUOTE);
        return ParseResult.failure(this.messageRegistry.getInvoked(LiteMessages.QUOTED_STRING_INVALID_FORMAT, invocation, error));
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<String> argument, SuggestionContext context) {
        SuggestionResult newResult = SuggestionResult.from(quoted(context.getCurrent()));

        ArgumentKey currentKey = argument.getKey();
        String name = argument.getName();

        if (name.equals(currentKey.getKey())) {
            return newResult;
        }

        Suggester<SENDER, String> suggester = suggesterRegistry.getSuggester(String.class, currentKey.withKey(name));
        SuggestionResult suggestionResult = suggester.suggest(invocation, argument, context);

        for (Suggestion suggestion : suggestionResult.getSuggestions()) {
            newResult.add(quoted(suggestion));
        }

        return newResult;
    }

    private Suggestion quoted(Suggestion suggestion) {
        List<String> multiLevelList = suggestion.multilevelList();
        List<String> newMultiLevelList = new ArrayList<>();

        for (int index = 0; index < multiLevelList.size(); index++) {
            String level = multiLevelList.get(index);
            String modifiedLevel = level;

            if (level.equals(quote) && multiLevelList.size() == 1) {
                newMultiLevelList.add(quote + quote);
                continue;
            }

            if (index == 0 && !level.startsWith(quote)) {
                modifiedLevel = "\"" + modifiedLevel;
            }

            if (index == multiLevelList.size() - 1 && !level.endsWith(quote)) {
                modifiedLevel = modifiedLevel + "\"";
            }

            newMultiLevelList.add(modifiedLevel);
        }

        return Suggestion.from(newMultiLevelList);
    }

    @Override
    public Range getRange(Argument<String> argument) {
        return Range.moreThan(1);
    }

}
