package dev.rollczi.litecommands;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserChained;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBase;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBaseChained;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.SuggesterChained;
import dev.rollczi.litecommands.bind.BindChainedProvider;
import dev.rollczi.litecommands.context.ContextChainedProvider;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.jetbrains.annotations.ApiStatus;

public interface LiteCommandsAdvanced<SENDER, SETTINGS extends PlatformSettings, B extends LiteCommandsAdvanced<SENDER, SETTINGS, B>> extends LiteCommandsBuilder<SENDER, SETTINGS, B> {

    /** Type raged argument */
    <T> B argumentParser(TypeRange<T> type, ArgumentKey key, Parser<SENDER, T> parser);

    <T> B argumentSuggestion(TypeRange<T> type, ArgumentKey key, SuggestionResult suggestion);

    <T> B argumentSuggester(TypeRange<T> type, ArgumentKey key, Suggester<SENDER, T> suggester);

    <T> B argument(TypeRange<T> type, ArgumentResolverBase<SENDER, T> resolver);

    <T> B argument(TypeRange<T> type, ArgumentKey key, ArgumentResolverBase<SENDER, T> resolver);

    /** Chained argument */
    @ApiStatus.Experimental
    <T> B argumentParser(Class<T> type, ParserChained<SENDER, T> parser);

    @ApiStatus.Experimental
    <T> B argumentParser(Class<T> type, ArgumentKey key, ParserChained<SENDER, T> parser);

    @ApiStatus.Experimental
    <T> B argumentParser(TypeRange<T> type, ArgumentKey key, ParserChained<SENDER, T> parser);

    @ApiStatus.Experimental
    <T> B argumentSuggester(Class<T> type, SuggesterChained<SENDER, T> suggester);

    @ApiStatus.Experimental
    <T> B argumentSuggester(Class<T> type, ArgumentKey key, SuggesterChained<SENDER, T> suggester);

    @ApiStatus.Experimental
    <T> B argumentSuggester(TypeRange<T> type, ArgumentKey key, SuggesterChained<SENDER, T> suggester);

    @ApiStatus.Experimental
    <T> B argument(Class<T> type, ArgumentResolverBaseChained<SENDER, T> resolver);

    @ApiStatus.Experimental
    <T> B argument(Class<T> type, ArgumentKey key, ArgumentResolverBaseChained<SENDER, T> resolver);

    @ApiStatus.Experimental
    <T> B argument(TypeRange<T> type, ArgumentResolverBaseChained<SENDER, T> resolver);

    @ApiStatus.Experimental
    <T> B argument(TypeRange<T> type, ArgumentKey key, ArgumentResolverBaseChained<SENDER, T> resolver);

    /** Chained context */
    @ApiStatus.Experimental
    <T> B context(Class<T> on, ContextChainedProvider<SENDER, T> bind);

    /** Chained bind */
    @ApiStatus.Experimental
    <T> B bind(Class<T> on, BindChainedProvider<T> bindProvider);

    LiteCommandsInternal<SENDER, SETTINGS> internal();

}
