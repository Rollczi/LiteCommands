package dev.rollczi.litecommands.chatgpt;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.join.JoinStringArgumentResolver;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
* @param <SENDER>
 */
class ChatGptStringSuggester<SENDER> extends JoinStringArgumentResolver<SENDER> implements Suggester<SENDER, String> {

    private final NavigableMap<String, String> suggestions = new TreeMap<>();
    private final ChatGptClient chatGptClient;

    private Instant lastRequest = Instant.now();

    public ChatGptStringSuggester(ChatGptClient chatGptClient) {
        this.chatGptClient = chatGptClient;
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<String> argument, SuggestionContext context) {
        String firstPart = context.getCurrent().multilevel();

        if (firstPart.length() < 4) {
            return SuggestionResult.of(firstPart);
        }

        Collection<String> suggestions = this.suggestions.subMap(firstPart, firstPart + Character.MAX_VALUE).values();
        SuggestionResult suggestionResult = SuggestionResult.of(suggestions);

        if (suggestions.contains(firstPart)) {
            return suggestionResult;
        }

        Instant unlockMoment = lastRequest.plus(Duration.ofSeconds(2));

        if (Instant.now().isBefore(unlockMoment)) {
            Logger.getLogger("LiteCommands").info("ChatGptStringSuggester is locked for " + Duration.between(Instant.now(), unlockMoment).toMillis() + "ms");
            return suggestionResult;
        }

        lastRequest = Instant.now();
        Logger.getLogger("LiteCommands").info("ChatGptStringSuggester is unlocked");
        String lastPart = chatGptClient.chat("You are a text suggester. Just finish the text in about 2 - 5 words. (don't forget the space at the beginning if needed)", "'" + firstPart + "'");

        if (lastPart.startsWith("'") && lastPart.endsWith("'")) {
            lastPart = lastPart.substring(1, lastPart.length() - 1);
        }

        String result = firstPart + lastPart;

        this.suggestions.put(firstPart, result);
        suggestionResult.add(Suggestion.of(result));

        return suggestionResult;
    }

    @Override
    public Range getRange(Argument<String> stringArgument) {
        return super.getRange(stringArgument);
    }
}
