package dev.rollczi.litecommands.chatgpt;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class ChatGptStringSuggester<SENDER> implements Suggester<SENDER, String> {

    private final Scheduler scheduler;
    private final ChatGptClient chatGptClient;
    private final ChatGptSettings settings;

    private final Map<String, NavigableMap<String, String>> suggestions = new HashMap<>();
    private final Cache<Identifier, Instant> lastRequestPerPlayer;

    ChatGptStringSuggester(Scheduler scheduler, ChatGptClient chatGptClient, ChatGptSettings settings) {
        this.scheduler = scheduler;
        this.chatGptClient = chatGptClient;
        this.settings = settings;
        this.lastRequestPerPlayer = Caffeine.newBuilder()
            .expireAfterWrite(settings.cooldown())
            .build();
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<String> argument, SuggestionContext context) {
        String firstPart = context.getCurrent().multilevel();
        String topic = argument.meta().get(LiteChatGptExtension.ARGUMENT_TOPIC);

        NavigableMap<String, String> navigableSuggestions = this.suggestions.computeIfAbsent(topic, key -> new TreeMap<>());
        Collection<String> suggestions = navigableSuggestions.subMap(firstPart, firstPart + Character.MAX_VALUE).values();

        if (!settings.shouldGenerate(firstPart)) {
            return SuggestionResult.of(suggestions);
        }

        SuggestionResult suggestionResult = SuggestionResult.of(suggestions);
        Identifier identifier = invocation.platformSender().getIdentifier();
        String commandStructure = this.showStructure(invocation, context);

        this.markGenerateRequest(identifier); // mark request to prevent spamming

        CompletableFuture<SuggestionResult> future = scheduler.supplyLater(SchedulerPoll.SUGGESTER, settings.cooldown(), () -> {
            if (!this.canGenerate(identifier)) {
                return null;
            }

            this.markGenerateRequest(identifier);
            String suggestion = this.generateNewSuggestion(commandStructure, topic, firstPart);

            navigableSuggestions.put(firstPart, suggestion);
            suggestionResult.add(Suggestion.of(suggestion));
            return suggestionResult;
        });

        try {
            return future.get(10, TimeUnit.SECONDS);
        }
        catch (ExecutionException exception) {
            return suggestionResult;
        }
        catch (InterruptedException | TimeoutException exception) {
            throw new RuntimeException(exception);
        }
    }

    private synchronized boolean canGenerate(Identifier identifier) {
        Instant perPlayerUnlockMoment = lastRequestPerPlayer.getIfPresent(identifier);

        return perPlayerUnlockMoment == null || Instant.now().isAfter(perPlayerUnlockMoment);
    }

    private synchronized void markGenerateRequest(Identifier identifier) {
        lastRequestPerPlayer.put(identifier, Instant.now().plus(settings.cooldown()));
    }

    private String generateNewSuggestion(String commandStructure, String topic, String firstPart) {
        String prompt = settings.prompt(commandStructure, topic);
        String lastPart = chatGptClient.chat(prompt, "'" + firstPart + "'");

        Logger.getLogger("LiteCommands").info("Generated suggestion: " + lastPart);

        if (lastPart.startsWith("'")) {
            lastPart = lastPart.substring(1);
        }

        if (lastPart.endsWith("'")) {
            lastPart = lastPart.substring(0, lastPart.length() - 1);
        }

        if (firstPart.endsWith(" ") && lastPart.startsWith(" ")) {
            lastPart = lastPart.substring(1);
        }

        return firstPart + lastPart;
    }

    private String showStructure(Invocation<SENDER> invocation, SuggestionContext context) {
        String label = invocation.label();
        List<String> args = invocation.arguments().asList();

        int exclude = context.getCurrent().lengthMultilevel();

        if (args.size() < exclude) {
            throw new IllegalStateException("Input arguments are less than exclude: " + exclude);
        }

        StringBuilder builder = new StringBuilder()
            .append(RawCommand.COMMAND_SLASH)
            .append(label);

        for (int i = 0; i < args.size() - exclude; i++) {
            builder
                .append(RawCommand.COMMAND_SEPARATOR)
                .append(args.get(i));
        }

        return builder.toString();
    }


}
