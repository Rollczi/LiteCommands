package dev.rollczi.litecommands.chatgpt;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.join.JoinStringArgumentResolver;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* @param <SENDER>
 */
class ChatGptStringSuggester<SENDER> extends JoinStringArgumentResolver<SENDER> implements Suggester<SENDER, String> {

    private final Logger logger = Logger.getLogger("ChatGptStringSuggester");
    private final ChatGptClient chatGptClient;
    private final Scheduler scheduler;

    private final NavigableMap<String, String> suggestions = new TreeMap<>();
    private final Map<Identifier, Instant> lastRequestPerPlayer = new HashMap<>();

    public ChatGptStringSuggester(ChatGptClient chatGptClient, Scheduler scheduler) {
        this.chatGptClient = chatGptClient;
        this.scheduler = scheduler;
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<String> argument, SuggestionContext context) {
        String firstPart = context.getCurrent().multilevel();

        if (firstPart.length() < 4 || firstPart.length() > 64 || !firstPart.endsWith(" ")) {
            return SuggestionResult.of(firstPart);
        }

        Collection<String> suggestions = this.suggestions.subMap(firstPart, firstPart + Character.MAX_VALUE).values();
        SuggestionResult suggestionResult = SuggestionResult.of(suggestions);

        Identifier identifier = invocation.platformSender().getIdentifier();
        Instant perPlayerUnlockMoment = lastRequestPerPlayer.get(identifier);

        lastRequestPerPlayer.put(identifier, Instant.now().plus(Duration.ofMillis(500)));

        if (perPlayerUnlockMoment != null && Instant.now().isBefore(perPlayerUnlockMoment)) {
            logger.log(Level.INFO, "Player " + identifier + " is trying to spam the server. Suggestion will be delayed by 500ms.");
            return suggestionResult;
        }

        this.generateNewSuggestion(firstPart);
        logger.log(Level.INFO, "Player " + identifier + " requested a new suggestion. It will be generated in the background.");
        return suggestionResult;
    }

    private void generateNewSuggestion(String firstPart) {
        scheduler.run(SchedulerPoll.ASYNCHRONOUS, () -> {
            logger.log(Level.INFO, "Generating new suggestion for: " + firstPart);
            String lastPart = chatGptClient.chat("You are a text suggester. Just finish the text in about 2 - 5 words. (don't forget the space at the beginning if needed)", "'" + firstPart + "'");

            if (lastPart.startsWith("'") && lastPart.endsWith("'")) {
                lastPart = lastPart.substring(1, lastPart.length() - 1);
            }

            String result = firstPart + lastPart;

            logger.log(Level.INFO, "Generated new suggestion: " + result);
            scheduler.run(SchedulerPoll.MAIN, () -> suggestions.put(firstPart, result));
        });
    }

    @Override
    public Range getRange(Argument<String> stringArgument) {
        return super.getRange(stringArgument);
    }
}
